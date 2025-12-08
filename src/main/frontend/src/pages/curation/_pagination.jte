@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel

@param Pagination<CurationViewModel> curationData
@param boolean oob = false

<div id="load-more" class="flex justify-center p-4" hx-swap-oob="${oob}">
    @if(curationData.isHasMore())
        <button class="btn"
                hx-get="/curations?page=${curationData.getNextPage()}&sort=${curationData.getOrderType()}"
                hx-target="#curation-list"
                hx-swap="beforeend"
        >
            <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
            <span class="[.htmx-request_&]:hidden">더 보기</span>
        </button>
    @endif
</div>
