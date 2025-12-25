@import xyz.goraebap.blog.infra.view_model.Pagination
@import xyz.goraebap.blog.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData
@param String loadMoreUrl = "/"
@param boolean oob = false

<div id="HX_LOAD_MORE" class="flex justify-center p-4" hx-swap-oob="${oob}">
    @if(postData.isHasMore())
        <button class="btn"
                hx-get="${loadMoreUrl}?page=${postData.getNextPage()}&sort=${postData.getOrderType()}"
                hx-target="#HX_POST_LIST"
                hx-swap="beforeend"
        >
            <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
            <span class="[.htmx-request_&]:hidden">더 보기</span>
        </button>
    @endif
</div>
