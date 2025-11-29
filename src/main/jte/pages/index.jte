@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import java.util.List

@param List<PostViewModel> posts
@param boolean hasMore
@param int nextPage
@param String orderType

@template.layout.default(title = "Home", content = @`
    <div class="flex flex-col items-center w-[600px]">
        <div class="flex gap-2 p-4">
            <a href="/"
               class="btn btn-sm ${orderType == null || orderType.isEmpty() ? "btn-active" : ""}">최근</a>
            <a href="/?orderType=POPULAR"
               class="btn btn-sm ${"POPULAR".equals(orderType) ? "btn-active" : ""}">인기</a>
        </div>

        <ul id="post-list" class="flex flex-col">
            @for(var post : posts)
                @template.fragments.postItem(post = post)
            @endfor
        </ul>

        @template.fragments.loadMoreButton(hasMore = hasMore, nextPage = nextPage, orderType = orderType)
    </div>
`)
