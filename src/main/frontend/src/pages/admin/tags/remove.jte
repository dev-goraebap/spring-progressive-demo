@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminTagViewModel

@param AdminTagViewModel tag

@template.widget.destroyConfirm(
    title = "태그 삭제",
    targetName = tag.getName(),
    targetType = "태그",
    apiUrl = "/api/v1/admin/tags/" + tag.getId(),
    successMessage = "태그가 삭제되었습니다.",
    errorMessage = "태그 삭제에 실패했습니다.",
    redirectUrl = "/admin/tags"
)
