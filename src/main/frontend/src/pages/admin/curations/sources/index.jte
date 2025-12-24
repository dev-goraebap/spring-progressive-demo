@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminCuratedSourceViewModel
@import java.time.format.DateTimeFormatter
@import java.util.List
@import java.util.Map

@param List<AdminCuratedSourceViewModel> sources
@param Map<String, Object> stats

!{var dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");}
!{var tabs = List.of(
    new String[]{"sources", "소스", "/admin/curations/sources"},
    new String[]{"items", "항목", "/admin/curations/items"}
);}

@template.shared.layout.admin(title = "RSS 큐레이션 - 소스 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "RSS 큐레이션")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "sources")
        </div>

        <%-- 통계 카드 --%>
        <div class="p-4 border-b border-base-300 flex gap-4 text-sm">
            <div class="flex items-center gap-2">
                <span class="text-base-content/60">총 소스:</span>
                <span class="font-semibold">${String.valueOf(stats.get("totalsources"))}</span>
            </div>
            <div class="flex items-center gap-2">
                <span class="text-base-content/60">활성:</span>
                <span class="font-semibold text-success">${String.valueOf(stats.get("activesources"))}</span>
            </div>
            <div class="flex items-center gap-2">
                <span class="text-base-content/60">총 항목:</span>
                <span class="font-semibold">${String.valueOf(stats.get("totalitems"))}</span>
            </div>
            <div class="flex-1"></div>
            <button type="button"
                    hx-post="/admin/curations/sources/fetch-all"
                    hx-disabled-elt="this"
                    class="btn btn-ghost btn-sm group">
                <span class="loading loading-spinner loading-sm hidden group-[.htmx-request]:inline-flex"></span>
                <span>전체 수집</span>
            </button>
            <button type="button"
                    hx-get="/admin/curations/sources/add"
                    hx-target="#HX_MODAL_CONTENT"
                    class="btn btn-primary btn-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                새 소스
            </button>
        </div>

        @template.shared.layout.admin.table(
            isEmpty = sources.isEmpty(),
            emptyMessage = "등록된 소스가 없습니다.",
            head = @`
                <th class="w-20">상태</th>
                <th class="min-w-32">이름</th>
                <th class="min-w-64">URL</th>
                <th class="w-24">항목 수</th>
                <th class="w-28">생성일</th>
                <th class="w-20">수집</th>
                <th class="w-16">관리</th>
            `,
            body = @`
                @for(var source : sources)
                    <tr class="hover:bg-base-200">
                        <td>
                            @if(source.isActive())
                                <span class="badge badge-success badge-sm">활성</span>
                            @else
                                <span class="badge badge-ghost badge-sm">비활성</span>
                            @endif
                        </td>
                        <td class="font-medium">${source.getName()}</td>
                        <td class="text-sm text-base-content/70 truncate max-w-64" title="${source.getUrl()}">${source.getUrl()}</td>
                        <td class="text-center">${source.getItemCount()}</td>
                        <td class="text-sm text-base-content/70">${source.getCreatedAt().format(dateFormat)}</td>
                        <td>
                            <button type="button"
                                    hx-post="/admin/curations/sources/${source.getId()}/fetch"
                                    hx-disabled-elt="this"
                                    class="btn btn-ghost btn-xs group">
                                <span class="loading loading-spinner loading-xs hidden group-[.htmx-request]:inline-flex"></span>
                                <span>수집</span>
                            </button>
                        </td>
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
                                    <li><a hx-get="/admin/curations/sources/${source.getId()}/edit" hx-target="#HX_MODAL_CONTENT">수정</a></li>
                                    <li><a hx-put="/admin/curations/sources/${source.getId()}/toggle">
                                        @if(source.isActive())
                                            비활성화
                                        @else
                                            활성화
                                        @endif
                                    </a></li>
                                    <li><a hx-get="/admin/curations/sources/${source.getId()}/remove" hx-target="#HX_MODAL_CONTENT" class="text-error">삭제</a></li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                @endfor
            `
        )

    </div>
`)
