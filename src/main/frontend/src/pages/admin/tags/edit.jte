@import xyz.goraebap.blog.infra.view_model.AdminTagViewModel

@param AdminTagViewModel tag

@template.pages.admin.tags.shared.form(
    title = "태그 수정",
    buttonText = "수정",
    id = tag.getId(),
    name = tag.getName()
)
