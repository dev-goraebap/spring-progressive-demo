@import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel
@import java.util.List

@param List<SeriesViewModel> seriesList

@template.shared.layout.default(title = "시리즈", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between sticky top-0 bg-base-100 z-[999]">
        <div>
            <h1 class="font-bold">시리즈</h1>
            <p class="text-xs hidden sm:block">연재 형태로 작성된 글들을 시리즈별로 모아볼 수 있습니다.</p>
        </div>
    </div>

    @if(!seriesList.isEmpty())
        <div class="grid grid-cols-2 sm:grid-cols-3 gap-4 p-4">
            @for(var series : seriesList)
                <a href="/series/${series.getSlug()}" class="group">
                    <%-- 책자 디자인 썸네일 --%>
                    <div class="relative aspect-[3/4] rounded-sm overflow-hidden shadow-lg
                                bg-gradient-to-r from-base-300 to-base-200
                                transform transition-transform group-hover:scale-105">
                        <%-- 책등 효과 --%>
                        <div class="absolute left-0 top-0 bottom-0 w-3 bg-base-300 shadow-inner"></div>

                        <%-- 썸네일 이미지 --%>
                        @if(series.getThumbnailUrl() != null)
                            <div class="absolute inset-0 ml-3">
                                @template.shared.components.img(
                                    url = series.getThumbnailUrl(),
                                    dominantColor = series.getThumbnailDominantColor(),
                                    alt = series.getName()
                                )
                            </div>
                        @else
                            <div class="absolute inset-0 ml-3 flex items-center justify-center bg-base-200">
                                <span class="text-base-content/30 text-4xl">📚</span>
                            </div>
                        @endif

                        <%-- 상태 뱃지 --%>
                        <div class="absolute top-2 right-2">
                            @if("COMPLETED".equals(series.getStatus()))
                                <span class="badge badge-success badge-sm">완결</span>
                            @elseif("PROGRESS".equals(series.getStatus()))
                                <span class="badge badge-primary badge-sm">연재중</span>
                            @elseif("PLAN".equals(series.getStatus()))
                                <span class="badge badge-ghost badge-sm">예정</span>
                            @endif
                        </div>
                    </div>

                    <%-- 시리즈 정보 --%>
                    <div class="mt-2">
                        <h2 class="font-semibold text-sm line-clamp-1">${series.getName()}</h2>
                        <p class="text-xs text-base-content/70 line-clamp-1">${series.getDescription()}</p>
                        <p class="text-xs text-base-content/50 mt-1">${series.getPostCount()}개의 글</p>
                    </div>
                </a>
            @endfor
        </div>
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            시리즈가 없습니다.
        </div>
    @endif
`)
