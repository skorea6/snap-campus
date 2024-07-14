package com.example.snapcampus.controller;

import com.example.snapcampus.dto.response.post.PostDetailDtoResponse;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.service.MemberService;
import com.example.snapcampus.service.PostService;
import com.example.snapcampus.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {
    private final MemberService memberService;
    private final SecurityService securityService;
    private final PostService postService;

    public UserController(MemberService memberService, SecurityService securityService, PostService postService) {
        this.memberService = memberService;
        this.securityService = securityService;
        this.postService = postService;
    }

    @GetMapping("/{userId}")
    public String info(@PathVariable String userId, Model model) {
        String memberUserId = securityService.getMemberUserId();
        Optional<Member> member = memberService.searchUser(userId); // userId null이나 빈칸일 경우?

        if(member.isEmpty()){
            return "redirect:/";
        }

        List<PostDetailDtoResponse> myPosts = postService.getMyPosts(userId);

        model.addAttribute("memberInfo", member.get().toDto());
        model.addAttribute("myPosts", myPosts);

        if(memberUserId != null && memberUserId.equals(userId)){
            model.addAttribute("isMyProfile", true);
        }

        return "member/info";
    }

}
