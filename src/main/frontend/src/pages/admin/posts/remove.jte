@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminPostViewModel

@param AdminPostViewModel post

@template.widget.destroyConfirm(
    title = "게시물 삭제",
    targetName = post.getTitle(),
    targetType = "게시물",
    actionUrl = "/admin/posts/" + post.getId()
)
