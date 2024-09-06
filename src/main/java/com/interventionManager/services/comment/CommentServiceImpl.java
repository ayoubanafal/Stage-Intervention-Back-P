package com.interventionManager.services.comment;

import com.interventionManager.entities.Comment;
import com.interventionManager.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    @Override
    public List<Comment> getCommentsByRequestId(Long requestId) {
        return commentRepository.findByRequestId(requestId);
    }
    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
    @Override
    public Comment createComment(Long userId, Long requestId, String text) {
        Comment comment = new Comment(userId, requestId, new Date(), text);
        return commentRepository.save(comment);
    }
    @Override
    public Comment updateComment(Long commentId, String text) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setText(text);
            return commentRepository.save(comment);
        }
        return null;
    }
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
