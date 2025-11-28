@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import java.util.List

@param List<PostViewModel> posts
@param boolean hasMore
@param int nextPage
@param String orderType

@for(var post : posts)
    @template.fragments.postItem(post = post)
@endfor

<!-- 더보기 버튼 OOB swap -->
@if(hasMore)
    <div id="load-more-container"
         hx-swap-oob="true"
         class="w-full flex justify-center p-4">
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
    </div>
@else
    <div id="load-more-container"
         hx-swap-oob="true"
         class="hidden">
    </div>
@endif
