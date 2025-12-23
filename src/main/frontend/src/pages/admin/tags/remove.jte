@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminTagViewModel

@param AdminTagViewModel tag

@template.widget.destroyConfirm(
    title = "태그 삭제",
    targetName = tag.getName(),
    targetType = "태그",
    actionUrl = "/admin/tags/" + tag.getId()
)
