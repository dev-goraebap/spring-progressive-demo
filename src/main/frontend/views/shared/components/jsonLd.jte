@import xyz.goraebap.blog.infra.view_model.PostViewModel
@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param PostViewModel post

<%--
    JSON-LD 구조화 데이터 (BlogPosting 스키마)
    - Google 리치 스니펫에 작성자, 발행일 등 표시
    - SEO 클릭률(CTR) 향상 효과
--%>
!{var datePublished = post.getPublishedAt() != null ? post.getPublishedAt().toLocalDate().toString() : "";}
<script type="application/ld+json">
{
    "@context": "https://schema.org",
    "@type": "BlogPosting",
    "headline": "${post.getTitle()}",
    "description": "${post.getSummary() != null ? post.getSummary().replace("\"", "\\\"") : ""}",
    "url": "${currentUrl()}",
    @if(post.getThumbnailUrl() != null)
    "image": "${post.getThumbnailUrl()}",
    @endif
    "datePublished": "${datePublished}",
    "dateModified": "${datePublished}",
    "author": {
        "@type": "Person",
        "name": "데브고래밥",
        "url": "https://dev.goraebap.xyz"
    },
    "publisher": {
        "@type": "Organization",
        "name": "데브고래밥",
        "logo": {
            "@type": "ImageObject",
            "url": "https://dev.goraebap.xyz/images/logo.png"
        }
    },
    "mainEntityOfPage": {
        "@type": "WebPage",
        "@id": "${currentUrl()}"
    }
}
</script>
