package com.teacher.qualification.dto.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCreateDto {

    private Long userId;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
