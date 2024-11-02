package com.teacher.qualification.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record PostCreateDto(
        String title,
        String content,
        String imageUrl
) {
}
