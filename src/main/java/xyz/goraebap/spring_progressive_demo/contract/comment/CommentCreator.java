package xyz.goraebap.spring_progressive_demo.contract.comment;

public interface CommentCreator {
    void create(String requestId, String postSlug, CreateCommentDto dto);
}