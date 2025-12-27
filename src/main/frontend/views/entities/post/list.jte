@import xyz.goraebap.blog.infra.view_model.PostViewModel
@import java.util.List

@param List<PostViewModel> items
@param String linkPrefix = "/posts"
@param boolean showTags = true

@for(var post: items)
    <a href="${linkPrefix}/${post.getSlug()}" class="flex gap-4 p-4 border-b border-base-300 hover:bg-base-200 transition-colors">
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
                @if(showTags && post.getTags() != null && !post.getTags().isEmpty())
                    <div class="hidden sm:flex gap-2">
                        @for(var tag : post.getTags())
                            <span>#${tag}</span>
                        @endfor
                    </div>
                @endif
            </div>
        </div>
    </a>
@endfor
