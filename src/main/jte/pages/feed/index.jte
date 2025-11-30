@import gg.jte.support.ForSupport
@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel

@param Pagination<PostViewModel> postData

@template.layout.default(title = "Home", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between">
        <div class="flex gap-4">
            <div class="w-6">@template.fragments.icon.filter()</div>
            <a href="/?orderType=viewCount">조회수</a>
            <a href="/?orderType=publishedAt">최근</a>
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
                <button class="text-lg font-bold mt-2">Read more</button>
            </div>
        </div>
    @endfor
`)
