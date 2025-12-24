@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminPostViewModel

@param AdminPostViewModel post

<div class="flex flex-col flex-1 min-h-0 max-h-[80vh]">
    @template.shared.components.modalHeader(title = post.getTitle())

    <div class="flex-1 overflow-y-auto">
        <%-- 썸네일 --%>
        @if(post.getThumbnailUrl() != null)
            <div class="w-full h-48 overflow-hidden flex items-center justify-center bg-base-200">
                @template.shared.components.img(
                    url = post.getThumbnailUrl(),
                    dominantColor = post.getThumbnailDominantColor(),
                    alt = post.getTitle()
                )
            </div>
        @endif

        <%-- 메타 정보 --%>
        <div class="p-4 border-b border-base-300 flex flex-wrap items-center gap-x-4 gap-y-1 text-sm text-base-content/70">
            @if(post.getPublishedAt() != null)
                <time>${post.getPublishedAt().toLocalDate().toString()}</time>
            @else
                <span class="text-warning">미발행</span>
            @endif
            <div class="flex items-center gap-1">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M2.036 12.322a1.012 1.012 0 0 1 0-.639C3.423 7.51 7.36 4.5 12 4.5c4.64 0 8.573 3.007 9.963 7.178.07.207.07.431 0 .639C20.577 16.49 16.64 19.5 12 19.5c-4.64 0-8.573-3.007-9.963-7.178Z"/>
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"/>
                </svg>
                <span>${post.getViewCount()}</span>
            </div>
            @if(post.getIsPublishedYn().equals("N"))
                <span class="badge badge-ghost badge-sm">비공개</span>
            @endif
            @if(post.getTags() != null && !post.getTags().isEmpty())
                <div class="flex flex-wrap gap-x-2">
                    @for(var tag : post.getTags().split(","))
                        <span class="text-primary">#${tag.trim()}</span>
                    @endfor
                </div>
            @endif
        </div>

        <%-- 본문 --%>
        <div class="p-4 prose prose-sm max-w-none">
            $unsafe{post.getContent()}
        </div>
    </div>
</div>
