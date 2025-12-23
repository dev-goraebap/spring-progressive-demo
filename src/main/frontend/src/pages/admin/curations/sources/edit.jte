@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminCuratedSourceViewModel

@param AdminCuratedSourceViewModel source

@template.pages.admin.curations.sources.shared.form(
    title = "소스 수정",
    buttonText = "저장",
    id = source.getId(),
    name = source.getName(),
    url = source.getUrl(),
    isActiveYn = source.getIsActiveYn()
)
