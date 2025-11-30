@import gg.jte.support.ForSupport
@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param Pagination<PostViewModel> postData

@template.layout.default(title = "Home", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between">
        <div class="flex gap-4">
            <div class="w-6">@template.fragments.icon.filter()</div>
            <a href="/?orderType=viewCount"
               class="${isQueryParam("orderType", "viewCount") || getQueryParam("orderType").isEmpty() ? "text-primary" : ""}">조회수</a>
            <a href="/?orderType=publishedAt" class="${isQueryParam("orderType", "publishedAt") ? "text-primary" : ""}">최근</a>
        </div>
    </div>

    @for(var entry: ForSupport.of(postData.getItems()))
        !{var post = entry.get();}
        <div class="${entry.isLast() ? "" : "border-b border-base-300"}">
            <div class="p-4 flex flex-col items-start gap-2">
                @if(post.getThumbnailUrl() != null)
                    <div class="w-full h-[200px] sm:h-[250px] rounded-xl overflow-hidden">
                        @template.fragments.uikit.img(
                        url = post.getThumbnailUrl(),
                        dominantColor = post.getThumbnailDominantColor(),
                        alt = post.getTitle()
                        )
                    </div>
                @endif
                <h2 class="text-xl font-bold">${post.getTitle()}</h2>
                <p class="text-sm line-clamp-2">${post.getSummary()}</p>
                <div class="flex flex-wrap gap-4 mt-2">
                    <div class="text-xs">#nestjs</div>
                    <div class="text-xs">#architecture</div>
                    <div class="text-xs">#libs</div>
                </div>
                <div class="w-full flex items-center justify-between mt-2">
                    <div class="flex items-center gap-4">
                        <!-- viewCount -->
                        <div class="flex gap-1 items-center">
                            <div class="w-4">
                                @template.fragments.icon.see()
                            </div>
                            <div class="text-sm">12 Reads</div>
                        </div>
                        <!-- commentCount -->
                        <div class="flex gap-1 items-center">
                            <div class="w-4">
                                @template.fragments.icon.comment()
                            </div>
                            <div class="text-sm">4</div>
                        </div>
                    </div>

                    <div class="text-sm">
                        2025/10/18 12:31
                    </div>
                </div>
            </div>
        </div>
    @endfor
`)
