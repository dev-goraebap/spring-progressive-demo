@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel
@import xyz.goraebap.spring_progressive_demo.app.client.dto.CurationIndexRequest

@param Pagination<CurationViewModel> curationData
@param CurationIndexRequest req

!{var pageDescription = "매일 업데이트되는 개발 트렌드를 전해드려요.";}

@template.shared.layout.main(
    title = "RSS 큐레이션",
    description = pageDescription,
    content = @`
    @template.shared.components.appHeader(
        title = "RSS 큐레이션",
        description = pageDescription,
        right = @`
            <div class="flex gap-2 text-sm shrink-0">
                <div class="w-5">@template.shared.svg.filter()</div>
                <a href="/curations?sort=publishedAt,desc"
                   class="${req.getSort().startsWith("publishedAt,desc") ? "text-primary" : ""}">최근</a>
                <a href="/curations?sort=publishedAt,asc"
                   class="${req.getSort().startsWith("publishedAt,asc") ? "text-primary" : ""}">오래된</a>
            </div>
        `
    )

    @if(!curationData.getItems().isEmpty())
        <div id="curation-list">
            @template.pages.curation._list(items = curationData.getItems())
        </div>
        @template.pages.curation._pagination(curationData = curationData)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
