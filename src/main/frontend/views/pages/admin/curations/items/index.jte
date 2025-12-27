@import xyz.goraebap.blog.infra.view_model.AdminCuratedItemViewModel
@import xyz.goraebap.blog.infra.view_model.AdminCuratedSourceViewModel
@import xyz.goraebap.blog.infra.view_model.Pagination
@import java.time.format.DateTimeFormatter
@import java.util.List

@param Pagination<AdminCuratedItemViewModel> itemsData
@param List<AdminCuratedSourceViewModel> sources
@param Long sourceId

!{var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");}
!{var tabs = List.of(
    new String[]{"sources", "소스", "/admin/curations/sources"},
    new String[]{"items", "항목", "/admin/curations/items"}
);}

@template.shared.layout.admin(title = "RSS 큐레이션 - 항목 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "RSS 큐레이션")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "items")
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <select name="sourceId"
                    class="select select-bordered select-sm w-full lg:w-48 order-1"
                    onchange="this.form.submit()">
                <option value="">모든 소스</option>
                @for(var source : sources)
                    @if(sourceId != null && sourceId.equals(source.getId()))
                        <option value="${source.getId()}" selected>${source.getName()}</option>
                    @else
                        <option value="${source.getId()}">${source.getName()}</option>
                    @endif
                @endfor
            </select>
            <div class="hidden lg:block flex-1 order-2"></div>
            <span class="text-sm text-base-content/60 order-3">총 ${itemsData.getTotalCount()}개</span>
        `)

        @template.shared.layout.admin.table(
            isEmpty = itemsData.getItems().isEmpty(),
            emptyMessage = "항목이 없습니다.",
            head = @`
                <th class="min-w-64">제목</th>
                <th class="w-32">소스</th>
                <th class="w-40">발행일</th>
                <th class="w-40">수집일</th>
                <th class="w-16">관리</th>
            `,
            body = @`
                @for(var item : itemsData.getItems())
                    <tr class="hover:bg-base-200">
                        <td>
                            <a href="${item.getLink()}" target="_blank" class="link link-hover font-medium line-clamp-1" title="${item.getTitle()}">
                                ${item.getTitle()}
                            </a>
                        </td>
                        <td class="text-sm text-base-content/70">${item.getSource()}</td>
                        <td class="text-sm text-base-content/70">${item.getPubDate().format(dateFormat)}</td>
                        <td class="text-sm text-base-content/70">${item.getCreatedAt().format(dateFormat)}</td>
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
                                    <li><a href="${item.getLink()}" target="_blank">열기</a></li>
                                    <li><a hx-get="/admin/curations/items/${item.getId()}/remove" hx-target="#HX_MODAL_CONTENT" class="text-error">삭제</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                @endfor
            `
        )

        @template.shared.layout.admin.pagination(
            totalCount = itemsData.getTotalCount(),
            currentPage = itemsData.getCurrentPage(),
            pageSize = itemsData.getPageSize(),
            totalPages = itemsData.getTotalPages(),
            baseUrl = "?sourceId=" + (sourceId != null ? sourceId : "")
        )

    </div>
`)
