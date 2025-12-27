@import xyz.goraebap.blog.infra.view_model.AdminCuratedSourceViewModel

@param AdminCuratedSourceViewModel source

@template.widget.destroyConfirm(
    title = "소스 삭제",
    targetName = source.getName(),
    targetType = "소스",
    actionUrl = "/admin/curations/sources/" + source.getId()
)
