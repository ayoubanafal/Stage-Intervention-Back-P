package com.interventionManager.controller.comment;

import com.interventionManager.entities.Comment;
import com.interventionManager.services.comment.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;

//    @GetMapping
//    public ResponseEntity<List<Comment>> getAllComments() {
//        return ResponseEntity.ok(commentService.getAllComments());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
//        Comment comment = commentService.getCommentById(id);
//        if (comment == null) return ResponseEntity.notFound().build();
//        return ResponseEntity.ok(comment);
//    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<Comment>> getCommentsByRequestId(@PathVariable Long requestId) {
        return ResponseEntity.ok(commentService.getCommentsByRequestId(requestId));
    }

    @PostMapping("/add")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment.getUserId(), comment.getRequestId(), comment.getText());
        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody String text) {
        Comment updatedComment = commentService.updateComment(id, text);
        if (updatedComment == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
