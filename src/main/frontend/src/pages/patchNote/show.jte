@import xyz.goraebap.blog.infra.view_model.PostViewModel

@param PostViewModel post

@template.shared.layout.main(
    title = post.getTitle(),
    description = post.getSummary(),
    ogImage = post.getThumbnailUrl(),
    content = @`
    @template.shared.components.appHeader(backAction = true, backUrl = "/patch-notes", title = "패치노트 목록")

    <article>
        @template.entities.post.detail(
            post = post,
            showTags = false,
            backUrl = "/patch-notes",
            backText = "패치노트 목록"
        )
    </article>
`)
