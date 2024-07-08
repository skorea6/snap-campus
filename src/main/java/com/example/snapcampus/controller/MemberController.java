package com.example.snapcampus.controller;

import com.example.snapcampus.dto.request.member.LoginDto;
import com.example.snapcampus.dto.request.member.MemberSignUpDtoRequest;
import com.example.snapcampus.dto.request.member.SignUpVerificationSendEmailDtoRequest;
import com.example.snapcampus.redis.dto.EmailVerificationDto;
import com.example.snapcampus.redis.dto.EmailVerificationDtoResponse;
import com.example.snapcampus.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "userId", required = false) String userId,
                        HttpServletRequest request, Model model) {
        basicFlashProcess(request, model, "");
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("userId", userId);
        }
        return "login/index";
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, Model model) {
        basicFlashProcess(request, model, new SignUpVerificationSendEmailDtoRequest());
        return "signup/index";
    }

    @GetMapping("/signup/check")
    public String signupCheck(HttpServletRequest request, Model model) {
        String verificationToken = (String) request.getSession().getAttribute("emailVerificationToken");

        if (verificationToken != null) {
            basicFlashProcess(request, model, "");
            if (!model.containsAttribute("email")) {
                return "redirect:member/signup";
            }
            return "signup/check";
        }else {
            return "redirect:member/signup";
        }
    }

    @GetMapping("/signup/final")
    public String signupFinal(HttpServletRequest request, Model model) {
        String verificationToken = (String) request.getSession().getAttribute("emailVerificationToken");

        if (verificationToken != null) {
            basicFlashProcess(request, model, new MemberSignUpDtoRequest());
            if (!model.containsAttribute("email")) {
                return "redirect:member/signup";
            }
            return "signup/final";
        }else {
            return "redirect:member/signup";
        }
    }

    /**
     * 회원가입 - 이메일 인증번호 발송
     */
    @PostMapping("/signup/verification/email/send")
    public String signUpVerificationSendEmail(@Valid @ModelAttribute SignUpVerificationSendEmailDtoRequest request, BindingResult result, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            FieldError fieldError = (FieldError) result.getAllErrors().get(0);
            redirectAttributes.addFlashAttribute("errorMessage", fieldError.getDefaultMessage());
        }else {
            try {
                EmailVerificationDtoResponse emailVerificationDtoResponse = memberService.signUpVerificationSendEmail(request);
                httpServletRequest.getSession().setAttribute("emailVerificationToken", emailVerificationDtoResponse.getToken()); // session 저장
                redirectAttributes.addFlashAttribute("email", request.getEmail());

                return "redirect:member/signup/check";
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "서버에 오류가 발생했습니다");
            }
        }
        redirectAttributes.addFlashAttribute("data", request);
        return "redirect:member/signup";
    }

    @PostMapping("/signup/verification/email/check")
    public String signUpVerificationCheckEmail(@RequestParam("email") String email, @RequestParam("verificationNumber") String verificationNumber, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String verificationToken = (String) httpServletRequest.getSession().getAttribute("emailVerificationToken");

        if (verificationToken != null) {
            try {
                EmailVerificationDto emailVerificationDto = memberService.signUpVerificationCheckEmail(verificationToken, verificationNumber);
                redirectAttributes.addFlashAttribute("email", emailVerificationDto.getEmail());
//                redirectAttributes.addFlashAttribute("message", "이메일 인증 성공!");

                return "redirect:member/signup/final";
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "서버에 오류가 발생했습니다");
            }
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("data", verificationNumber);
            return "redirect:member/signup/check";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "다시 시도해주세요.");
            return "redirect:member/signup";
        }
    }

    @PostMapping("/signup/final")
    public String signUpFinalAction(@Valid @ModelAttribute MemberSignUpDtoRequest request, BindingResult result, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String verificationToken = (String) httpServletRequest.getSession().getAttribute("emailVerificationToken");

        if (verificationToken != null) {
            if (result.hasErrors()) {
                Map<String, String> errors = result.getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (message1, message2) -> message1));

                if(request.getPasswordRe() != null && !request.getPassword().equals(request.getPasswordRe())){
                    errors.put("passwordRe", "비밀번호가 일치하지 않습니다.");
                }

                redirectAttributes.addFlashAttribute("errors", errors);
//                FieldError fieldError = (FieldError) result.getAllErrors().get(0);
//                redirectAttributes.addFlashAttribute("errorMessage", fieldError.getDefaultMessage());
            }else {
                try {
                    memberService.signUp(request, verificationToken);
                    redirectAttributes.addFlashAttribute("message", "회원가입이 완료 되었습니다! 로그인하세요.");

                    return "redirect:member/login";
                } catch (IllegalArgumentException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "서버에 오류가 발생했습니다");
                }
            }
            redirectAttributes.addFlashAttribute("email", request.getEmail());
            redirectAttributes.addFlashAttribute("data", request);

            return "redirect:member/signup/final";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "다시 시도해주세요.");
            return "redirect:member/signup";
        }
    }

    // request.getSession().removeAttribute("emailVerificationToken");

    private static void basicFlashProcess(HttpServletRequest request, Model model, Object data) {
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        if (inputFlashMap != null) {
            model.addAllAttributes(inputFlashMap);
        }
        if (!model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", "");
        }
        if (!model.containsAttribute("message")) {
            model.addAttribute("message", "");
        }
        if (!model.containsAttribute("data")) {
            model.addAttribute("data", data);
        }
    }
}
