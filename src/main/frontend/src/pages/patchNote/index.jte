@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import xyz.goraebap.spring_progressive_demo.app.client.dto.PatchNoteIndexRequest

@param Pagination<PostViewModel> postData
@param PatchNoteIndexRequest req

!{var pageDescription = "블로그의 모든 주목할 만한 변경 사항이 기록됩니다.";}

@template.shared.layout.main(
    title = "패치노트",
    description = pageDescription,
    content = @`
    @template.shared.components.appHeader(
        title = "패치노트",
        description = pageDescription,
        right = @`
            <div class="flex gap-2 text-sm shrink-0">
                <div class="w-5">@template.shared.svg.filter()</div>
                <a href="/patch-notes?sort=publishedAt,desc"
                   class="${req.getSort().startsWith("publishedAt,desc") ? "text-primary" : ""}">최근</a>
                <a href="/patch-notes?sort=publishedAt,asc"
                   class="${req.getSort().startsWith("publishedAt,asc") ? "text-primary" : ""}">오래된</a>
            </div>
        `
    )

    @if(!postData.getItems().isEmpty())
        <div id="HX_POST_LIST">
            @template.entities.post.list(items = postData.getItems(), linkPrefix = "/patch-notes", showTags = false)
        </div>
        @template.entities.post.pagination(postData = postData, loadMoreUrl = "/patch-notes")
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
