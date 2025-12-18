package xyz.goraebap.spring_progressive_demo.contract.comment;

import xyz.goraebap.spring_progressive_demo.app.client.dto.CreateCommentRequest;

public interface CommentCreator {
    void create(String requestId, String postSlug, CreateCommentRequest request);
}