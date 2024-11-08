package com.teacher.qualification.dto.question.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public record OptionRequestDto(
        Integer number, // 몇 번 옵션인지
        String content // 옵션 내용
) {
}
