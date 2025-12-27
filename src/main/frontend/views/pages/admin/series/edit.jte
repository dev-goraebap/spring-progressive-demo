@import xyz.goraebap.blog.infra.view_model.AdminSeriesViewModel
@import java.time.format.DateTimeFormatter

@param AdminSeriesViewModel series

!{var dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");}

@template.pages.admin.series.shared.form(
    title = "시리즈 수정",
    buttonText = "수정",
    id = series.getId(),
    name = series.getName(),
    slug = series.getSlug(),
    description = series.getDescription() != null ? series.getDescription() : "",
    status = series.getStatus(),
    isPublishedYn = series.getIsPublishedYn(),
    publishedAt = series.getPublishedAt() != null ? series.getPublishedAt().format(dateTimeFormat) : "",
    thumbnailUrl = series.getThumbnailUrl()
)
