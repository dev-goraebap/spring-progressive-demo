@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminPostViewModel

@param AdminPostViewModel post

@template.widget.destroyConfirm(
    title = "게시물 삭제",
    targetName = post.getTitle(),
    targetType = "게시물",
    apiUrl = "/api/v1/admin/posts/" + post.getId(),
    successMessage = "게시물이 삭제되었습니다.",
    errorMessage = "게시물 삭제에 실패했습니다.",
    redirectUrl = "/admin/posts"
)
