@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminTagViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminTagIndexRequest
@import java.time.format.DateTimeFormatter
@import java.util.List

@param Pagination<AdminTagViewModel> tagData
@param AdminTagIndexRequest req

!{var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");}
!{var tabs = List.of(
    new String[]{"posts", "포스트", "/admin/posts"},
    new String[]{"series", "시리즈", "/admin/series"},
    new String[]{"tags", "태그", "/admin/tags"}
);}

@template.shared.layout.admin(title = "태그 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "블로그 콘텐츠")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "tags")
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <input type="text"
                   name="name"
                   value="${req.getName()}"
                   placeholder="태그 검색..."
                   class="input input-bordered input-sm w-full lg:w-64 order-1"/>
            <button type="submit" class="btn btn-ghost btn-sm order-2">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                </svg>
            </button>
            <div class="hidden lg:block flex-1 order-3"></div>
<%--            <a href="/admin/tags/new" class="btn btn-primary btn-sm w-full lg:w-auto order-4">--%>
<%--                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"--%>
<%--                     stroke="currentColor" class="size-4">--%>
<%--                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>--%>
<%--                </svg>--%>
<%--                새 태그--%>
<%--            </a>--%>
            <div x-data>
                <button type="button" @click="$store.modal.onOpen('/admin/tags/new')" class="btn btn-primary btn-sm w-full lg:w-auto order-4">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                         stroke="currentColor" class="size-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                    </svg>
                    새 태그
                </button>
            </div>
        `)

        @template.shared.layout.admin.table(
        isEmpty = tagData.getItems().isEmpty(),
        emptyMessage = "태그가 없습니다.",
        head = @`
            <th class="min-w-40">이름</th>
            <th class="w-24 lg:w-28">게시물</th>
            <th class="w-28 lg:w-32">생성일</th>
            <th class="w-16 lg:w-20">관리</th>
        `,
        body = @`
            @for(var tag : tagData.getItems())
                <tr class="hover:bg-base-200">
                    <td class="font-medium">${tag.getName()}</td>
                    <td class="text-center">${tag.getPostCount()}</td>
                    <td class="text-sm text-base-content/70">${tag.getCreatedAt().format(dateFormat)}</td>
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
                                <li><a href="/admin/tags/${tag.getId()}/edit">수정</a></li>
                                <li><a class="text-error">삭제</a></li>
                            </ul>
                        </div>
                    </td>
                </tr>
            @endfor
        `
        )

        @template.shared.layout.admin.pagination(
        totalCount = tagData.getTotalCount(),
        currentPage = tagData.getCurrentPage(),
        pageSize = tagData.getPageSize(),
        totalPages = tagData.getTotalPages(),
        baseUrl = "?name=" + req.getName()
        )

    </div>
`)
