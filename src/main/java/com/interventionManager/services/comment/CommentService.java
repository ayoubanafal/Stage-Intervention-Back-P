package com.interventionManager.services.comment;

import com.interventionManager.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments();
    List<Comment> getCommentsByRequestId(Long requestId);
    Comment getCommentById(Long id);
    Comment createComment(Long userId, Long requestId, String text);
    Comment updateComment(Long commentId, String text);
    void deleteComment(Long id);
}
