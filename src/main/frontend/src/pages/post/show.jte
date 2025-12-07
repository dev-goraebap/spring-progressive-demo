@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param PostViewModel post

@template.shared.layout.default(title = post.getTitle(), content = @`
    <div class="w-full p-4 border-b border-base-300 flex items-center gap-2 sticky top-0 bg-base-100 z-[999]">
        <a href="javascript:history.back()" class="flex items-center gap-1">
            <div class="w-5">@template.shared.svg.back()</div>
            <span>돌아가기</span>
        </a>
    </div>

    <article>
        @if(post.getThumbnailUrl() != null)
            <div class="w-full h-[180px] sm:h-[300px] overflow-hidden flex items-center justify-center">
                @template.shared.components.img(
                    url = post.getThumbnailUrl(),
                    dominantColor = post.getThumbnailDominantColor(),
                    alt = post.getTitle()
                )
            </div>
        @endif

        <div class="p-4">
            <div class="flex items-center gap-4 text-sm text-base-content/70 mb-4">
                @if(post.getPublishedAt() != null)
                    <time>${post.getPublishedAt().toLocalDate().toString()}</time>
                @endif
                <div class="flex items-center gap-1">
                    <div class="w-4">
                        @template.shared.svg.see()
                    </div>
                    <span>${post.getViewCount()}</span>
                </div>
                <div class="flex items-center gap-1">
                    <div class="w-4">
                        @template.shared.svg.comment()
                    </div>
                    <span>${post.getCommentCount()}</span>
                </div>
                @if(post.getTags() != null && !post.getTags().isEmpty())
                    <div class="flex flex-wrap gap-x-2">
                        @for(var tag : post.getTags())
                            <span>#${tag}</span>
                        @endfor
                    </div>
                @endif
            </div>

            <div class="prose prose-sm sm:prose-base max-w-none">
                $unsafe{post.getContent()}
            </div>
        </div>

        <%-- 댓글 영역 --%>
        <div class="p-4 border-t border-base-300">
            <div hx-get="/comments/${post.getSlug()}"
                 hx-trigger="load"
                 hx-swap="innerHTML">
                <div class="flex justify-center py-8">
                    <span class="loading loading-spinner loading-md"></span>
                </div>
            </div>
        </div>
    </article>
`)
