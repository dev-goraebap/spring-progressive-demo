@import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesDetailViewModel

@param SeriesDetailViewModel series

@template.shared.layout.main(
    title = series.getName(),
    description = series.getDescription(),
    ogImage = series.getThumbnailUrl(),
    content = @`
    @template.shared.components.appHeader(backAction = true, backUrl = "/series", title = "시리즈 목록")

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
        @template.entities.post.list(items = series.getPosts(), showTags = false)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            아직 작성된 글이 없습니다.
        </div>
    @endif
`)
