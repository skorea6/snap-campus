package com.example.snapcampus.dto.request.event;

import com.example.snapcampus.entity.Event;
import com.example.snapcampus.util.DateUtil;
import com.example.snapcampus.util.PatternUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventAddDtoRequest {
    @NotNull(message = "mapId는 필수 입력 값입니다.")
    private Long mapId;

    @NotBlank(message = "이벤트 이름은 필수 입력 값입니다.")
    @Size(max = 20, message = "이벤트 이름은 최대 20자여야 합니다.")
    private String name;

    @NotBlank(message = "이벤트 설명은 필수 입력 값입니다.")
    @Size(max = 200, message = "이벤트 설명은 최대 200자여야 합니다.")
    private String description;

    @Size(max = 20, message = "이벤트 주최자는 최대 20자여야 합니다.")
    private String organizer;

    @Pattern(regexp = PatternUtil.DATE_PATTERN, message = PatternUtil.DATE_MESSAGE)
    private String startDate;

    @Pattern(regexp = PatternUtil.DATE_PATTERN, message = PatternUtil.DATE_MESSAGE)
    private String stopDate;

    public Event toEntity(){
        return new Event(
                name, description, DateUtil.stringToLocalDate(startDate), DateUtil.stringToLocalDate(stopDate), organizer
        );
    }
}

