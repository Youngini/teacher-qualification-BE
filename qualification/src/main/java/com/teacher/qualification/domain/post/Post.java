package com.teacher.qualification.domain.post;

import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.domain.comment.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 질의 응답 게시판을 위한 entity
@Setter
@Getter
@Entity
public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private String title;
    @Lob
    private String content;
    //private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}