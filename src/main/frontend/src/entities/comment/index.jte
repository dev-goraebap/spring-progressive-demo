@import xyz.goraebap.spring_progressive_demo.infra.view_model.CommentViewModel
@import java.util.List

@param String postSlug
@param List<CommentViewModel> comments
@param int commentCount
@param String requestId
@param boolean success = false

<div id="commentEntity-section">
    <%-- 성공 메시지 --%>
    @if(success)
        <div class="alert alert-success mb-4" x-data="{ show: true }" x-show="show" x-init="setTimeout(() => show = false, 3000)">
            댓글 달아주셔서 감사합니다!
        </div>
    @endif

    <%-- 댓글 폼 --%>
    @template.entities.comment.form(postSlug = postSlug, commentCount = commentCount, requestId = requestId)

    <%-- 댓글 목록 --%>
    @template.entities.comment.list(comments = comments)
</div>
