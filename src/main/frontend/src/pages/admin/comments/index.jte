@import xyz.goraebap.blog.app.admin.dto.AdminCommentIndexRequest
@import xyz.goraebap.blog.infra.view_model.AdminCommentViewModel
@import xyz.goraebap.blog.infra.view_model.Pagination
@import java.time.format.DateTimeFormatter
@import java.util.Arrays
@import java.util.List

@param Pagination<AdminCommentViewModel> commentData
@param AdminCommentIndexRequest req

!{var dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd HH:mm");}
!{List<String[]> tabs = Arrays.<String[]>asList(
    new String[]{"comments", "댓글", "/admin/comments"}
);}

@template.shared.layout.admin(title = "댓글 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "커뮤니티")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "comments")
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <input type="text"
                   name="search"
                   value="${req.getSearch()}"
                   placeholder="닉네임 또는 내용 검색..."
                   class="input input-bordered input-sm w-full lg:w-64 order-1"/>
            <button type="submit" class="btn btn-ghost btn-sm order-2">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                </svg>
            </button>
            <div class="flex-1 order-3"></div>
        `)

        @template.shared.layout.admin.table(
            isEmpty = commentData.getItems().isEmpty(),
            emptyMessage = "댓글이 없습니다.",
            head = @`
                <th class="w-24">닉네임</th>
                <th class="min-w-48">내용</th>
                <th class="w-40">게시물</th>
                <th class="w-28">작성일</th>
                <th class="w-16">관리</th>
            `,
            body = @`
                @for(var comment : commentData.getItems())
                    <tr class="hover:bg-base-200"
                        data-avatar-no="${comment.getAvatarNo()}"
                        x-data="commentItem($el.dataset.avatarNo)">
                        <td>
                            <div class="flex items-center gap-2">
                                <span class="text-lg" x-text="avatar"></span>
                                <span class="font-medium">${comment.getNickname()}</span>
                            </div>
                        </td>
                        <td>
                            <div class="max-w-sm truncate" title="${comment.getComment()}">${comment.getComment()}</div>
                        </td>
                        <td>
                            @if(comment.getPostUrl() != null)
                                <a href="${comment.getPostUrl()}"
                                   target="_blank"
                                   class="text-primary hover:underline text-sm truncate block max-w-36"
                                   title="${comment.getPostTitle()}">
                                    ${comment.getPostTitle()}
                                </a>
                            @else
                                <span class="text-base-content/40">-</span>
                            @endif
                        </td>
                        <td class="text-sm text-base-content/70">${comment.getCreatedAt().format(dateTimeFormat)}</td>
                        <td>
                            <button type="button"
                                    hx-get="/admin/comments/${comment.getId()}/remove"
                                    hx-target="#HX_MODAL_CONTENT"
                                    class="btn btn-ghost btn-xs text-error">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"/>
                                </svg>
                            </button>
                        </td>
                    </tr>
                @endfor
            `
        )

        @template.shared.layout.admin.pagination(
            totalCount = commentData.getTotalCount(),
            currentPage = commentData.getCurrentPage(),
            pageSize = commentData.getPageSize(),
            totalPages = commentData.getTotalPages(),
            baseUrl = "?search=" + req.getSearch()
        )

    </div>
`)
