@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostSeriesNavViewModel

@param PostViewModel post
@param PostSeriesNavViewModel seriesNav = null
@param boolean showTags = true
@param String backUrl = null
@param String backText = "돌아가기"
@param String postLinkPrefix = "/posts"

<%-- 썸네일 --%>
@if(post.getThumbnailUrl() != null)
    <div class="w-full h-[11.25rem] sm:h-[18.75rem] overflow-hidden flex items-center justify-center">
        @template.shared.components.img(
            url = post.getThumbnailUrl(),
            dominantColor = post.getThumbnailDominantColor(),
            alt = post.getTitle()
        )
    </div>
@endif

<%-- 메타 정보 --%>
<div class="p-4">
    <div class="flex flex-wrap items-center gap-x-4 gap-y-1 text-sm text-base-content/70 mb-4">
        @if(post.getPublishedAt() != null)
            <time class="flex-shrink-0">${post.getPublishedAt().toLocalDate().toString()}</time>
        @endif
        <div class="flex items-center gap-1 flex-shrink-0">
            <div class="w-4">
                @template.shared.svg.see()
            </div>
            <span>${post.getViewCount()}</span>
        </div>
        <div class="flex items-center gap-1 flex-shrink-0">
            <div class="w-4">
                @template.shared.svg.comment()
            </div>
            <span>${post.getCommentCount()}</span>
        </div>
        @if(showTags && post.getTags() != null && !post.getTags().isEmpty())
            <div class="flex flex-wrap gap-x-2">
                @for(var tag : post.getTags())
                    <span>#${tag}</span>
                @endfor
            </div>
        @endif
    </div>

    <%-- 본문 --%>
    <div class="prose prose-sm sm:prose-base max-w-none">
        $unsafe{post.getContent()}
    </div>
</div>

<%-- 시리즈 이전/다음 네비게이션 (하단) --%>
@if(seriesNav != null && (seriesNav.getPrev() != null || seriesNav.getNext() != null))
    <div class="p-4 border-t border-base-300">
        <p class="text-xs text-base-content/60 mb-3">이 글은 <a href="/series/${seriesNav.getSeriesSlug()}" class="text-primary hover:underline">${seriesNav.getSeriesName()}</a> 시리즈의 일부입니다.</p>
        <div class="flex gap-2">
            @if(seriesNav.getPrev() != null)
                <a href="${postLinkPrefix}/${seriesNav.getPrev().getSlug()}"
                   class="flex-1 p-3 rounded-lg bg-base-200 hover:bg-base-300 transition-colors">
                    <div class="flex items-center gap-2 text-xs text-base-content/60 mb-1">
                        <div class="w-3">@template.shared.svg.back()</div>
                        <span>이전 글</span>
                    </div>
                    <div class="text-sm font-medium line-clamp-1">${seriesNav.getPrev().getTitle()}</div>
                </a>
            @else
                <div class="flex-1"></div>
            @endif
            @if(seriesNav.getNext() != null)
                <a href="${postLinkPrefix}/${seriesNav.getNext().getSlug()}"
                   class="flex-1 p-3 rounded-lg bg-base-200 hover:bg-base-300 transition-colors text-right">
                    <div class="flex items-center justify-end gap-2 text-xs text-base-content/60 mb-1">
                        <span>다음 글</span>
                        <div class="w-3 rotate-180">@template.shared.svg.back()</div>
                    </div>
                    <div class="text-sm font-medium line-clamp-1">${seriesNav.getNext().getTitle()}</div>
                </a>
            @endif
        </div>
    </div>
@endif

<%-- 댓글 영역 --%>
<div class="p-4 border-t border-base-300">
    <div hx-get="/comments/${post.getSlug()}"
         hx-trigger="load once"
         hx-swap="innerHTML">
        <div class="flex justify-center py-8">
            <span class="loading loading-spinner loading-md"></span>
        </div>
    </div>
</div>

<%-- 하단 돌아가기 버튼 --%>
<div class="p-4 border-t border-base-300">
    @if(backUrl != null)
        <a href="${backUrl}" class="btn btn-block btn-outline">
            <div class="w-5">@template.shared.svg.back()</div>
            ${backText}
        </a>
    @else
        <a href="javascript:history.back()" class="btn btn-block btn-outline">
            <div class="w-5">@template.shared.svg.back()</div>
            ${backText}
        </a>
    @endif
</div>
