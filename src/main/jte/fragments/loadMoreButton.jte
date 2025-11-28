@param boolean hasMore
@param int nextPage
@param String orderType

<div id="load-more-container"
     class="${hasMore ? "w-full flex justify-center p-4" : "hidden"}">
    @if(hasMore)
        <button class="btn"
                hx-get="/"
                hx-boost="false"
                hx-vals='{ "page": ${nextPage}, "orderType": "${orderType != null ? orderType : ""}" }'
                hx-target="#post-list"
                hx-swap="beforeend transition:false"
                hx-indicator="#load-more-spinner">
            <span id="load-more-spinner" class="loading loading-spinner htmx-indicator"></span>
            더보기
        </button>
    @endif
</div>
