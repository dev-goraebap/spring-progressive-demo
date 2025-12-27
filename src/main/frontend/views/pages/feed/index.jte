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
        description = pageDescription,
        right = @`
            <div class="flex gap-2 text-sm shrink-0">
                <div class="w-5">@template.shared.svg.filter()</div>
                <a href="/?sort=viewCount,desc"
                   class="${req.getSort().startsWith("viewCount") ? "text-primary" : ""}">조회수</a>
                <a href="/?sort=publishedAt,desc"
                   class="${req.getSort().startsWith("publishedAt") ? "text-primary" : ""}">최근</a>
            </div>
        `
    )

    <div class="border-b border-base-300">
        @template.pages.feed.index.curationNews(items = curationItems)
    </div>

    <%-- 최근 패치노트 --%>
    @if(latestPatchNote != null)
        <a href="/patch-notes/${latestPatchNote.getSlug()}" class="block bg-info/10 border-b border-info/30 hover:bg-info/20 transition-colors">
            <div class="px-4 pt-3 pb-1">
                <span class="text-xs font-semibold text-info">최근 패치노트</span>
            </div>
            <div class="flex gap-4 p-4 pt-0">
                <%-- 썸네일 --%>
                <div class="w-24 sm:w-40 h-16 sm:h-24 rounded overflow-hidden flex-shrink-0 bg-base-200">
                    @if(latestPatchNote.getThumbnailUrl() != null)
                        @template.shared.components.img(
                            url = latestPatchNote.getThumbnailUrl(),
                            dominantColor = latestPatchNote.getThumbnailDominantColor(),
                            alt = latestPatchNote.getTitle()
                        )
                    @else
                        <div class="w-full h-full flex items-center justify-center text-base-content/20">
                            <div class="w-8">@template.shared.svg.image()</div>
                        </div>
                    @endif
                </div>

                <%-- 포스트 정보 --%>
                <div class="flex flex-col justify-center min-w-0 flex-1">
                    <h2 class="font-bold text-base sm:text-lg line-clamp-1">${latestPatchNote.getTitle()}</h2>
                    <p class="text-sm text-base-content/70 line-clamp-2 mt-1">${latestPatchNote.getSummary()}</p>
                    <div class="flex items-center gap-4 text-xs text-base-content/50 mt-2">
                        @if(latestPatchNote.getPublishedAt() != null)
                            <span>${latestPatchNote.getPublishedAt().toLocalDate().toString()}</span>
                        @endif
                        <div class="flex items-center gap-1">
                            <div class="w-3">@template.shared.svg.see()</div>
                            <span>${latestPatchNote.getViewCount()}</span>
                        </div>
                        <div class="flex items-center gap-1">
                            <div class="w-3">@template.shared.svg.comment()</div>
                            <span>${latestPatchNote.getCommentCount()}</span>
                        </div>
                    </div>
                </div>
            </div>
        </a>
    @endif

    @if(!postData.getItems().isEmpty())
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
