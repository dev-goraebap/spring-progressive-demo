@import xyz.goraebap.blog.infra.view_model.Pagination
@import xyz.goraebap.blog.infra.view_model.PostViewModel
@import xyz.goraebap.blog.infra.view_model.CurationViewModel
@import xyz.goraebap.blog.app.client.dto.FeedIndexRequest
@import java.util.List

@param Pagination<PostViewModel> postData
@param List<CurationViewModel> curationItems
@param PostViewModel latestPatchNote = null
@param FeedIndexRequest req

!{var firstPost = postData.getItems().isEmpty() ? null : postData.getItems().get(0);}
!{var feedOgImage = firstPost != null && firstPost.getThumbnailUrl() != null ? firstPost.getThumbnailUrl() : null;}
!{var pageDescription = "개발 관련 글과 팁을 공유합니다.";}

@template.shared.layout.main(
    title = "피드",
    description = pageDescription,
    ogImage = feedOgImage,
    content = @`
    @template.shared.components.appHeader(
        title = "피드",
        description = pageDescription
    )

    <div class="border-b border-base-300">
        @template.pages.feed.index.highlights(items = curationItems, latestPatchNote = latestPatchNote)
    </div>

    @if(!postData.getItems().isEmpty())
        <%-- 정렬 탭 --%>
        <div class="flex gap-6 px-4 border-b border-base-300">
            <a href="/?sort=publishedAt,desc"
               class="py-3 text-sm font-medium border-b-2 -mb-px ${req.getSort().startsWith("publishedAt") ? "border-primary text-primary" : "border-transparent text-base-content/60 hover:text-base-content"}">
                최근
            </a>
            <a href="/?sort=viewCount,desc"
               class="py-3 text-sm font-medium border-b-2 -mb-px ${req.getSort().startsWith("viewCount") ? "border-primary text-primary" : "border-transparent text-base-content/60 hover:text-base-content"}">
                조회수
            </a>
        </div>

        <div id="HX_POST_LIST">
            @template.entities.post.list(items = postData.getItems())
        </div>
        @template.entities.post.pagination(postData = postData)
    @else
        <div class="w-full flex flex-col items-center p-10 gap-4">
            <div class="w-2/3">
                @template.shared.svg.empty01()
            </div>
            데이터가 없습니다.
        </div>
    @endif
`)
