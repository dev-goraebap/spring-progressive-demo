@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminPostViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.app.post.dto.AdminPostIndexRequest
@import java.time.format.DateTimeFormatter
@import java.util.List

@param Pagination<AdminPostViewModel> postData
@param AdminPostIndexRequest req

!{var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");}
!{var tabs = List.of(
    new String[]{"posts", "포스트", "/admin/posts"},
    new String[]{"series", "시리즈", "/admin/series"},
    new String[]{"tags", "태그", "/admin/tags"}
);}

@template.shared.layout.admin(title = "포스트 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "블로그 콘텐츠")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "posts")
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <input type="text"
                   name="title"
                   value="${req.getTitle()}"
                   placeholder="제목 검색..."
                   class="input input-bordered input-sm w-full lg:w-64 order-1"/>
            <select name="postType" class="select select-bordered select-sm flex-1 lg:flex-none lg:w-28 order-2">
                <option value="">전체 타입</option>
                @if(req.getPostType().equals("post"))
                    <option value="post" selected>일반</option>
                @else
                    <option value="post">일반</option>
                @endif
                @if(req.getPostType().equals("patch-note"))
                    <option value="patch-note" selected>패치노트</option>
                @else
                    <option value="patch-note">패치노트</option>
                @endif
            </select>
            <select name="isPublishedYn" class="select select-bordered select-sm flex-1 lg:flex-none lg:w-24 order-3">
                <option value="">전체</option>
                @if(req.getIsPublishedYn().equals("Y"))
                    <option value="Y" selected>공개</option>
                @else
                    <option value="Y">공개</option>
                @endif
                @if(req.getIsPublishedYn().equals("N"))
                    <option value="N" selected>비공개</option>
                @else
                    <option value="N">비공개</option>
                @endif
            </select>
            <button type="submit" class="btn btn-ghost btn-sm order-4">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                </svg>
            </button>
            <div class="hidden lg:block flex-1 order-5"></div>
            <a href="/admin/posts/new" class="btn btn-primary btn-sm w-full lg:w-auto order-6">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                새 포스트
            </a>
        `)

        @template.shared.layout.admin.table(
            isEmpty = postData.getItems().isEmpty(),
            emptyMessage = "포스트가 없습니다.",
            head = @`
                <th class="w-16 lg:w-20">썸네일</th>
                <th class="min-w-40">제목</th>
                <th class="w-20 lg:w-24">타입</th>
                <th class="w-28 lg:w-32">태그</th>
                <th class="w-16 lg:w-20">상태</th>
                <th class="w-16 lg:w-20">조회수</th>
                <th class="w-24 lg:w-28">발행일</th>
                <th class="w-24 lg:w-28">생성일</th>
                <th class="w-16 lg:w-20">관리</th>
            `,
            body = @`
                @for(var post : postData.getItems())
                    <tr class="hover:bg-base-200">
                        <td>
                            @if(post.getThumbnailUrl() != null)
                                <div class="w-12 h-8 rounded overflow-hidden">
                                    @template.shared.components.img(
                                    url = post.getThumbnailUrl(),
                                    dominantColor = post.getThumbnailDominantColor(),
                                    alt = post.getTitle()
                                    )
                                </div>
                            @else
                                <div class="w-12 h-8 rounded bg-base-300 flex items-center justify-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor"
                                         class="size-4 text-base-content/40">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"/>
                                    </svg>
                                </div>
                            @endif
                        </td>
                        <td>
                            <div class="font-medium">${post.getTitle()}</div>
                            @if(post.getSummary() != null && !post.getSummary().isEmpty())
                                <div class="text-xs text-base-content/60 truncate max-w-60" title="${post.getSummary()}">${post.getSummary()}</div>
                            @endif
                        </td>
                        <td>
                            @if(post.getPostType().equals("post"))
                                <span class="badge badge-sm">일반</span>
                            @else
                                <span class="badge badge-sm badge-ghost">패치노트</span>
                            @endif
                        </td>
                        <td>
                            @if(post.getTags() != null && !post.getTags().isEmpty())
                                <span class="text-xs text-base-content/70">${post.getTags()}</span>
                            @else
                                <span class="text-xs text-base-content/40">-</span>
                            @endif
                        </td>
                        <td>
                            @if(post.isPublished())
                                <span class="badge badge-sm badge-success">공개</span>
                            @else
                                <span class="badge badge-sm badge-ghost">비공개</span>
                            @endif
                        </td>
                        <td class="text-center">${post.getViewCount()}</td>
                        <td class="text-sm text-base-content/70">
                            @if(post.getPublishedAt() != null)
                                ${post.getPublishedAt().format(dateFormat)}
                            @else
                                <span class="text-base-content/40">-</span>
                            @endif
                        </td>
                        <td class="text-sm text-base-content/70">${post.getCreatedAt().format(dateFormat)}</td>
                        <td>
                            <div class="dropdown dropdown-end">
                                <div tabindex="0" role="button" class="btn btn-ghost btn-xs">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"
                                         stroke-width="1.5" stroke="currentColor" class="size-4">
                                        <path stroke-linecap="round" stroke-linejoin="round"
                                              d="M12 6.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 12.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5ZM12 18.75a.75.75 0 1 1 0-1.5.75.75 0 0 1 0 1.5Z"/>
                                    </svg>
                                </div>
                                <ul tabindex="0"
                                    class="dropdown-content menu bg-base-100 rounded-box z-[1] w-32 p-2 shadow">
                                    <li><a href="/admin/posts/${post.getId()}/edit">수정</a></li>
                                    <li><a class="text-error">삭제</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                @endfor
            `
        )

        @template.shared.layout.admin.pagination(
            totalCount = postData.getTotalCount(),
            currentPage = postData.getCurrentPage(),
            pageSize = postData.getPageSize(),
            totalPages = postData.getTotalPages(),
            baseUrl = "?title=" + req.getTitle() + "&postType=" + req.getPostType() + "&isPublishedYn=" + req.getIsPublishedYn()
        )

    </div>
`)
