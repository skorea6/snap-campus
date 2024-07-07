package com.example.snapcampus.dto.request.comment;


import com.example.snapcampus.util.PatternUtil;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentListDtoRequest {
    @NotNull(message = "postId는 필수 입력 값입니다.")
    private Long postId;

    @NotNull(message = "page는 필수 입력 값입니다.")
    @Min(1)
    private Long page = 1L;

    @NotNull(message = "pageSize는 필수 입력 값입니다.")
    @Min(5)
    @Max(50)
    private Long pageSize = 5L;

    @NotBlank(message = "sortDirection은 필수 입력 값입니다.")
    @Pattern(regexp = PatternUtil.SORT_DIRECTION_PATTERN, message = PatternUtil.SORT_DIRECTION_MESSAGE)
    private String sortDirection = "asc";

    public CommentListDtoRequest() {
    }

    public CommentListDtoRequest(Long postId, Long page, Long pageSize, String sortDirection) {
        this.postId = postId;
        this.page = page;
        this.pageSize = pageSize;
        this.sortDirection = sortDirection;
    }
}
