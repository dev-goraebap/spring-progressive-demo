@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel
@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*
@import java.util.List

@param Pagination<PostViewModel> postData
@param List<CurationViewModel> curationItems

@template.layout.default(title = "피드", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between sticky top-0 bg-base-100 z-[999]">
        <h1 class="font-bold">피드</h1>
        <div class="flex gap-2 text-sm shrink-0">
            <div class="w-5">@template.shared.svg.filter()</div>
            <a href="/?sort=viewCount,desc"
               class="${getQueryParam("sort").startsWith("viewCount") || getQueryParam("sort").isEmpty() ? "text-primary" : ""}">조회수</a>
            <a href="/?sort=publishedAt,desc"
               class="${getQueryParam("sort").startsWith("publishedAt") ? "text-primary" : ""}">최근</a>
        </div>
    </div>

    <div class="border-b border-base-300">
        @template.pages.feed._curationNews(items = curationItems)
    </div>

    @if(!postData.getItems().isEmpty())
        @template.pages.feed._list(postData = postData)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
