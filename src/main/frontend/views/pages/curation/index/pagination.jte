@import xyz.goraebap.blog.infra.view_model.Pagination
@import xyz.goraebap.blog.infra.view_model.CurationViewModel

@param Pagination<CurationViewModel> curationData
@param boolean oob = false

<div id="HX_LOAD_MORE" class="flex justify-center p-4" hx-swap-oob="${oob}">
    @if(curationData.isHasMore())
        <button class="btn"
                hx-get="/curations?page=${curationData.getNextPage()}&sort=${curationData.getOrderType()}"
                hx-target="#HX_CURATION_LIST"
                hx-swap="beforeend"
        >
            <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
            <span class="[.htmx-request_&]:hidden">더 보기</span>
        </button>
    @endif
</div>
