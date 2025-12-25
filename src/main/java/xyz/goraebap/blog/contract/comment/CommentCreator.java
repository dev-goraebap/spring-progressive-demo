package xyz.goraebap.blog.contract.comment;

public interface CommentCreator {
    void create(String requestId, String postSlug, CreateCommentDto dto);
}