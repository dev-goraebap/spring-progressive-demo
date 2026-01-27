@import xyz.goraebap.blog.infra.view_model.PostViewModel
@import xyz.goraebap.blog.infra.view_model.PostSeriesNavViewModel

@param PostViewModel post
@param PostSeriesNavViewModel seriesNav = null

@template.shared.layout.main(
    title = post.getTitle(),
    description = post.getSummary(),
    ogImage = post.getThumbnailUrl(),
    content = @`
    <%-- JSON-LD 구조화 데이터 --%>
    @template.shared.components.jsonLd(post = post)

    @template.shared.components.appHeader(backAction = true, title = "돌아가기")

    <%-- 시리즈 네비게이션 (상단) --%>
    @if(seriesNav != null)
        <div class="p-4 border-b border-base-300 bg-base-200/50">
            <a href="/series/${seriesNav.getSeriesSlug()}" class="flex gap-3 items-center hover:opacity-80 transition-opacity">
                <%-- 시리즈 썸네일 --%>
                <div class="relative w-12 aspect-[3/4] rounded-sm overflow-hidden shadow flex-shrink-0
                            bg-gradient-to-r from-base-300 to-base-200">
                    <div class="absolute left-0 top-0 bottom-0 w-1 bg-base-300 shadow-inner"></div>
                    @if(seriesNav.getThumbnailUrl() != null)
                        <div class="absolute inset-0 ml-1">
                            @template.shared.components.img(
                                url = seriesNav.getThumbnailUrl(),
                                dominantColor = seriesNav.getThumbnailDominantColor(),
                                alt = seriesNav.getSeriesName()
                            )
                        </div>
                    @else
                        <div class="absolute inset-0 ml-1 flex items-center justify-center bg-base-200">
                            <span class="text-base-content/30 text-sm">📚</span>
                        </div>
                    @endif
                </div>
                <%-- 시리즈 정보 --%>
                <div class="flex flex-col gap-0.5 min-w-0 flex-1">
                    <span class="text-xs text-base-content/60">시리즈</span>
                    <span class="font-semibold text-sm truncate">${seriesNav.getSeriesName()}</span>
                    <span class="text-xs text-base-content/60">${seriesNav.getCurrentOrder()} / ${seriesNav.getTotalCount()}</span>
                </div>
                <div class="w-5 text-base-content/40">
                    @template.shared.svg.chevronRight()
                </div>
            </a>
        </div>
    @endif

    <article>
        @template.entities.post.detail(
            post = post,
            seriesNav = seriesNav,
            showTags = true,
            postLinkPrefix = "/posts"
        )
    </article>
`)
