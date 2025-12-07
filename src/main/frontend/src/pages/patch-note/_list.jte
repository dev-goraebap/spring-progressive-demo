@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData

@for(var post: postData.getItems())
    <a href="/patch-notes/${post.getSlug()}" class="flex gap-4 p-4 border-b border-base-300 hover:bg-base-200 transition-colors">
        <%-- 썸네일 --%>
        <div class="w-24 sm:w-40 h-16 sm:h-24 rounded overflow-hidden flex-shrink-0 bg-base-200">
            @if(post.getThumbnailUrl() != null)
                @template.shared.components.img(
                    url = post.getThumbnailUrl(),
                    dominantColor = post.getThumbnailDominantColor(),
                    alt = post.getTitle()
                )
            @else
                <div class="w-full h-full flex items-center justify-center text-base-content/20">
                    <div class="w-8">@template.shared.svg.image()</div>
                </div>
            @endif
        </div>

        <%-- 포스트 정보 --%>
        <div class="flex flex-col justify-center min-w-0 flex-1">
            <h2 class="font-bold text-base sm:text-lg line-clamp-1">${post.getTitle()}</h2>
            <p class="text-sm text-base-content/70 line-clamp-2 mt-1">${post.getSummary()}</p>
            <div class="flex items-center gap-4 text-xs text-base-content/50 mt-2">
                @if(post.getPublishedAt() != null)
                    <span>${post.getPublishedAt().toLocalDate().toString()}</span>
                @endif
                <div class="flex items-center gap-1">
                    <div class="w-3">@template.shared.svg.see()</div>
                    <span>${post.getViewCount()}</span>
                </div>
                <div class="flex items-center gap-1">
                    <div class="w-3">@template.shared.svg.comment()</div>
                    <span>${post.getCommentCount()}</span>
                </div>
            </div>
        </div>
    </a>
@endfor
@if(postData.isHasMore())
    <div id="load-more" class="flex justify-center p-4">
        <button class="btn"
                hx-get="/patch-notes?page=${postData.getNextPage()}&sort=${postData.getOrderType()}"
                hx-target="#load-more"
                hx-swap="outerHTML"
        >
            <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
            <span class="[.htmx-request_&]:hidden">더 보기</span>
        </button>
    </div>
@endif
