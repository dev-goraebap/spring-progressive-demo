@import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel
@import java.util.List

@param List<PostViewModel> posts
@param boolean hasMore
@param int nextPage
@param String orderType

@template.layout.default(title = "Home", content = @`
    <div class="w-full p-4 border-b border-base-300 flex justify-between">
        <div class="flex gap-4">
            <div class="w-6">@template.fragments.icon.filter()</div>
            <a href="/">조회수</a>
            <a href="/">최근</a>
        </div>
    </div>
`)
