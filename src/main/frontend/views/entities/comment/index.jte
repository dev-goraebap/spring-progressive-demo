@import xyz.goraebap.blog.infra.view_model.CommentViewModel
@import java.util.List

@param String postSlug
@param List<CommentViewModel> comments
@param int commentCount
@param String requestId

<div id="HX_COMMENT_SECTION">
    <%-- 댓글 폼 --%>
    @template.entities.comment.form(postSlug = postSlug, commentCount = commentCount, requestId = requestId)

    <%-- 댓글 목록 --%>
    @template.entities.comment.list(comments = comments)
</div>
