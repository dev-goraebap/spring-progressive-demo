@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel

@param Pagination<CurationViewModel> curationData

@for(var item: curationData.getItems())
    <div class="border-b border-base-300">
        <div class="p-4 flex flex-col items-start gap-2">
            <a href="${item.getLink()}" target="_blank" class="text-lg font-bold hover:text-primary">${item.getTitle()}</a>
            @if(item.getSnippet() != null)
                <p class="text-sm line-clamp-2">${item.getSnippet()}</p>
            @endif
            <div class="w-full flex items-center justify-between mt-2">
                <div class="text-sm text-base-content/70">${item.getSource()}</div>
                @if(item.getPubDate() != null)
                    <div class="text-sm">${item.getPubDate().toLocalDate().toString()}</div>
                @endif
            </div>
        </div>
    </div>
@endfor
@if(curationData.isHasMore())
    <div id="load-more" class="flex justify-center p-4">
        <button class="btn"
                hx-get="/curations?page=${curationData.getNextPage()}&sort=${curationData.getOrderType()}"
                hx-target="#load-more"
                hx-swap="outerHTML"
        >
            <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
            <span class="[.htmx-request_&]:hidden">더 보기</span>
        </button>
    </div>
@endif
