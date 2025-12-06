@import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesDetailViewModel

@param SeriesDetailViewModel series

@template.shared.layout.default(title = series.getName(), content = @`
    <div class="w-full p-4 border-b border-base-300 flex items-center gap-2 sticky top-0 bg-base-100 z-[999]">
        <a href="/series" class="flex items-center gap-1">
            <div class="w-5">@template.shared.svg.back()</div>
            <span>시리즈 목록</span>
        </a>
    </div>

    <%-- 시리즈 헤더 --%>
    <div class="p-4 border-b border-base-300">
        <div class="flex gap-4">
            <%-- 썸네일 (책자 스타일) --%>
            <div class="relative w-24 aspect-[3/4] rounded-sm overflow-hidden shadow-lg flex-shrink-0
                        bg-gradient-to-r from-base-300 to-base-200">
                <div class="absolute left-0 top-0 bottom-0 w-2 bg-base-300 shadow-inner"></div>
                @if(series.getThumbnailUrl() != null)
                    <div class="absolute inset-0 ml-2">
                        @template.shared.components.img(
                            url = series.getThumbnailUrl(),
                            dominantColor = series.getThumbnailDominantColor(),
                            alt = series.getName()
                        )
                    </div>
                @else
                    <div class="absolute inset-0 ml-2 flex items-center justify-center bg-base-200">
                        <span class="text-base-content/30 text-2xl">📚</span>
                    </div>
                @endif
            </div>

            <%-- 시리즈 정보 --%>
            <div class="flex flex-col justify-center gap-1">
                <h1 class="font-bold text-lg">${series.getName()}</h1>
                <div class="flex items-center gap-2 text-sm text-base-content/70">
                    @if("COMPLETED".equals(series.getStatus()))
                        <span class="badge badge-success badge-sm">완결</span>
                    @elseif("PROGRESS".equals(series.getStatus()))
                        <span class="badge badge-primary badge-sm">연재중</span>
                    @elseif("PLAN".equals(series.getStatus()))
                        <span class="badge badge-ghost badge-sm">예정</span>
                    @endif
                    <span>${series.getPosts().size()}개의 글</span>
                    @if(series.getPublishedAt() != null)
                        <span class="text-xs text-base-content/50">${series.getPublishedAt().toLocalDate().toString()} 시작</span>
                    @endif
                </div>
            </div>
        </div>
    </div>

    <%-- 포스트 목록 --%>
    @if(!series.getPosts().isEmpty())
        <div class="divide-y divide-base-300">
            @for(var post : series.getPosts())
                <a href="/posts/${post.getSlug()}" class="flex gap-3 p-4 hover:bg-base-200 transition-colors">
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
                                <div class="w-6">@template.shared.svg.image()</div>
                            </div>
                        @endif
                    </div>

                    <%-- 포스트 정보 --%>
                    <div class="flex flex-col justify-center min-w-0 flex-1">
                        <h2 class="font-semibold text-sm line-clamp-1">${post.getTitle()}</h2>
                        @if(post.getSummary() != null)
                            <p class="text-xs text-base-content/70 line-clamp-2 mt-1">${post.getSummary()}</p>
                        @endif
                        <div class="flex items-center gap-3 text-xs text-base-content/50 mt-1">
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
        </div>
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            아직 작성된 글이 없습니다.
        </div>
    @endif
`)
