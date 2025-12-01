@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel
@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param Pagination<CurationViewModel> curationData

@template.layout.default(title = "RSS 큐레이션", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between sticky top-0 bg-base-100">
        <div>
            <h1 class="font-bold">RSS 큐레이션</h1>
            <p class="text-xs hidden sm:block">매일 업데이트되는 개발 트렌드를 전해드려요.</p>
        </div>
        <div class="flex gap-2 text-sm shrink-0">
            <div class="w-5">@template.fragments.svg.filter()</div>
            <a href="/curations?sort=publishedAt,desc"
               class="${getQueryParam("sort").startsWith("publishedAt,desc") || getQueryParam("sort").isEmpty() ? "text-primary" : ""}">최근</a>
            <a href="/curations?sort=publishedAt,asc"
               class="${getQueryParam("sort").startsWith("publishedAt,asc") ? "text-primary" : ""}">오래된</a>
        </div>
    </div>

    @if(!curationData.getItems().isEmpty())
        @template.pages.curation._list(curationData = curationData)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.fragments.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
