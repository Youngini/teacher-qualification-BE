package com.teacher.qualification.service.comment;

import com.teacher.qualification.domain.comment.Comment;
import com.teacher.qualification.domain.post.Post;
import com.teacher.qualification.dto.comment.CommentInfo;
import com.teacher.qualification.dto.comment.ModifyCommentDto;
import com.teacher.qualification.dto.comment.WriteCommentDto;
import com.teacher.qualification.repository.comment.CommentRepository;
import com.teacher.qualification.repository.post.PostRepository;
import com.teacher.qualification.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    // 댓글 불러오기
    public List<CommentInfo> getAllComment(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (post.getComments() == null) {
            return null;
        }
        List<Comment> comments = post.getComments();
        List<CommentInfo> commentInfoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentInfoList.add(convertToDto(comment));
        }
        return commentInfoList;
    }

    private CommentInfo convertToDto(Comment comment) {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setComment_id(comment.getId());
        commentInfo.setContent(comment.getComment());
        commentInfo.setAuthor(comment.getUser().getNickname());
        commentInfo.setCreate_time(comment.getCreatedAt());
        return commentInfo;
    }

    // 댓글 작성
    public void createComment(Long userId, Long postId, WriteCommentDto commentDto) {
        Comment comment = new Comment();

        comment.setUser(userRepository.getReferenceById(userId));
        comment.setPost(postRepository.getReferenceById(postId));
        comment.setComment(commentDto.getComment());
        commentRepository.save(comment);
    }

    // 댓글 수정
    public boolean updateComment(Long userId, Long commentId, ModifyCommentDto commentDto) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if(comment.getUser().getId() != userId){
            return false;
        }
        comment.setComment(commentDto.getComment());
        commentRepository.save(comment);
        return true;
    }

    // 댓글 삭제
    public boolean deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if(comment.getUser().getId() != userId){
            return false;
        }
        commentRepository.delete(comment);
        return true;
    }


}

