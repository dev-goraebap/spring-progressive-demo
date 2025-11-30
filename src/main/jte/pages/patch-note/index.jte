@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param Pagination<PostViewModel> postData

@template.layout.default(title = "패치노트", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between sticky top-0 bg-base-100">
        <div>
            <h1 class="font-bold">패치노트</h1>
            <p class="text-xs">블로그의 모든 주목할 만한 변경 사항이 기록됩니다.</p>
        </div>
        <div class="flex gap-2 text-sm">
            <div class="w-5">@template.fragments.svg.filter()</div>
            <a href="/patch-notes?sort=publishedAt,desc"
               class="${getQueryParam("sort").startsWith("publishedAt,desc") || getQueryParam("sort").isEmpty() ? "text-primary" : ""}">최근</a>
            <a href="/patch-notes?sort=publishedAt,asc"
               class="${getQueryParam("sort").startsWith("publishedAt,asc") ? "text-primary" : ""}">오래된</a>
        </div>
    </div>

    @if(!postData.getItems().isEmpty())
        @template.pages.patch-note._list(postData = postData)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.fragments.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
