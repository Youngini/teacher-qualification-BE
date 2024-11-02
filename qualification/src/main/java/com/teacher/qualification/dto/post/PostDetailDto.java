package com.teacher.qualification.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public record PostDetailDto(
        Long userId,
        String nickname,
        String title,
        String content,
        LocalDateTime lastModifiedAt
) {
}