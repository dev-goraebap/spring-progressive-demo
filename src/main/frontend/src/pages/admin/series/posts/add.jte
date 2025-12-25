@import xyz.goraebap.blog.infra.view_model.AvailablePostViewModel
@import java.time.format.DateTimeFormatter
@import java.util.List

@param Long seriesId
@param String keyword
@param List<AvailablePostViewModel> posts

!{var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");}

<div class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = "포스트 추가")

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <form hx-get="/admin/series/${seriesId}/posts/add"
              hx-target="#HX_MODAL_CONTENT"
              hx-trigger="input changed delay:300ms from:#keyword-input"
              class="flex gap-2">
            <input type="text"
                   id="keyword-input"
                   name="keyword"
                   value="${keyword != null ? keyword : ""}"
                   placeholder="포스트 제목 검색..."
                   class="input input-bordered input-sm flex-1"
                   autofocus />
        </form>

        @if(posts.isEmpty())
            <div class="text-center py-8 text-base-content/60">
                @if(keyword != null && !keyword.isEmpty())
                    <p>검색 결과가 없습니다</p>
                @else
                    <p>추가 가능한 포스트가 없습니다</p>
                @endif
            </div>
        @else
            <ul class="space-y-2 max-h-80 overflow-y-auto">
                @for(var post : posts)
                    <li class="flex items-center gap-3 p-3 bg-base-200 rounded-lg hover:bg-base-300">
                        <div class="flex-1 min-w-0">
                            <div class="font-medium truncate">${post.getTitle()}</div>
                            <div class="text-xs text-base-content/60">
                                ${post.getPublishedAt() != null ? post.getPublishedAt().format(dateFormat) : "미발행"}
                            </div>
                        </div>
                        <button type="button"
                                hx-post="/admin/series/${seriesId}/posts"
                                hx-vals='{"postId": "${post.getId()}"}'
                                class="btn btn-primary btn-sm">
                            추가
                        </button>
                    </li>
                @endfor
            </ul>
        @endif
    </div>
</div>
