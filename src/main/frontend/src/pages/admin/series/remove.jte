@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminSeriesViewModel

@param AdminSeriesViewModel series

@template.widget.destroyConfirm(
    title = "시리즈 삭제",
    targetName = series.getName(),
    targetType = "시리즈",
    actionUrl = "/admin/series/" + series.getId()
)
