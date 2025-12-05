@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData

@for(var post: postData.getItems())
    <div class="border-b border-base-300">
        <div class="p-4 flex flex-col items-start gap-2">
            @if(post.getThumbnailUrl() != null)
                <div class="w-full h-[200px] sm:h-[250px] rounded-xl overflow-hidden">
                    @template.shared.components.img(
                    url = post.getThumbnailUrl(),
                    dominantColor = post.getThumbnailDominantColor(),
                    alt = post.getTitle()
                    )
                </div>
            @endif
            <h2 class="text-xl font-bold">${post.getTitle()}</h2>
            <p class="text-sm line-clamp-2">${post.getSummary()}</p>

            <div class="w-full flex items-center justify-between mt-2">
                <div class="flex items-center gap-4">
                    <div class="flex gap-1 items-center">
                        <div class="w-4">
                            @template.shared.svg.see()
                        </div>
                        <div class="text-sm">${post.getViewCount()}</div>
                    </div>
                    <div class="flex gap-1 items-center">
                        <div class="w-4">
                            @template.shared.svg.comment()
                        </div>
                        <div class="text-sm">${post.getCommentCount()}</div>
                    </div>
                </div>
                @if(post.getPublishedAt() != null)
                    <div class="text-sm">
                        ${post.getPublishedAt().toLocalDate().toString()}
                    </div>
                @endif
            </div>
        </div>
    </div>
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
