package com.example.snapcampus.dto.request.post;

import com.example.snapcampus.entity.Event;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.util.DateUtil;
import com.example.snapcampus.util.PatternUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostAddRequest {
    @NotNull(message = "eventId는 필수 입력 값입니다.")
    private Long eventId;

    @NotBlank(message = "사진 제목은 필수 입력 값입니다.")
    @Size(max = 50, message = "사진 제목은 최대 50자여야 합니다.")
    private String title;

    @NotBlank(message = "사진 설명은 필수 입력 값입니다.")
    @Size(max = 200, message = "이벤트 설명은 최대 200자여야 합니다.")
    private String description;

    @Size(max = 20, message = "과는 최대 20자여야 합니다.")
    private String department;

    private List<MultipartFile> images;

    public Post toEntity(){
        return new Post(
                title, description, department
        );
    }
}
