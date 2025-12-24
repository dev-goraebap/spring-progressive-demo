@import xyz.goraebap.spring_progressive_demo.app.admin.dto.AdminBlockedIpIndexRequest
@import xyz.goraebap.spring_progressive_demo.infra.view_model.BlockedIpViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.Pagination
@import java.time.format.DateTimeFormatter
@import java.util.Arrays
@import java.util.List

@param Pagination<BlockedIpViewModel> blockedIpData
@param AdminBlockedIpIndexRequest req

!{var dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd HH:mm");}
!{List<String[]> tabs = Arrays.<String[]>asList(
    new String[]{"blocked-ips", "접근관리", "/admin/blocked-ips"}
);}

@template.shared.layout.admin(title = "접근관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "보안")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "blocked-ips")
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <input type="text"
                   name="search"
                   value="${req.getSearch()}"
                   placeholder="IP 또는 사유 검색..."
                   class="input input-bordered input-sm w-full lg:w-64 order-1"/>
            <button type="submit" class="btn btn-ghost btn-sm order-2">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5"
                     stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round"
                          d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"/>
                </svg>
            </button>
            <div class="flex-1 order-3"></div>
            <button type="button"
                    hx-get="/admin/blocked-ips/add"
                    hx-target="#HX_MODAL_CONTENT"
                    class="btn btn-primary btn-sm order-4">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                IP 차단
            </button>
        `)

        <div id="BLOCKED_IP_TABLE">
            @template.shared.layout.admin.table(
                isEmpty = blockedIpData.getItems().isEmpty(),
                emptyMessage = "차단된 IP가 없습니다.",
                head = @`
                    <th class="w-36">IP</th>
                    <th class="min-w-48">사유</th>
                    <th class="w-20">경로</th>
                    <th class="w-20">상태</th>
                    <th class="w-28">만료일</th>
                    <th class="w-28">생성일</th>
                    <th class="w-28">관리</th>
                `,
                body = @`
                    @for(var item : blockedIpData.getItems())
                        <tr class="hover:bg-base-200">
                            <td>
                                <code class="text-sm">${item.getIpAddress()}</code>
                            </td>
                            <td>
                                <div class="max-w-sm truncate text-sm" title="${item.getReason()}">
                                    ${item.getReason() != null ? item.getReason() : "-"}
                                </div>
                            </td>
                            <td>
                                <span class="badge badge-sm ${item.getBlockedBy().equals("auto") ? "badge-warning" : "badge-info"}">
                                    ${item.getBlockedByLabel()}
                                </span>
                            </td>
                            <td>
                                @if(item.isActive())
                                    <span class="badge badge-sm badge-error">차단중</span>
                                @elseif(item.isExpired())
                                    <span class="badge badge-sm badge-ghost">만료</span>
                                @else
                                    <span class="badge badge-sm badge-ghost">해제</span>
                                @endif
                            </td>
                            <td class="text-sm text-base-content/70">
                                ${item.getExpiresAt() != null ? item.getExpiresAt().format(dateTimeFormat) : "영구"}
                            </td>
                            <td class="text-sm text-base-content/70">
                                ${item.getCreatedAt().format(dateTimeFormat)}
                            </td>
                            <td>
                                <div class="flex gap-1">
                                    @if(item.isActive() && !item.isPermanent())
                                        <button type="button"
                                                hx-patch="/admin/blocked-ips/${item.getId()}/permanent"
                                                hx-confirm="영구 차단으로 변경하시겠습니까?"
                                                class="btn btn-ghost btn-xs"
                                                title="영구 차단">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M16.5 10.5V6.75a4.5 4.5 0 1 0-9 0v3.75m-.75 11.25h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H6.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z"/>
                                            </svg>
                                        </button>
                                    @endif
                                    @if(item.isActive())
                                        <button type="button"
                                                hx-patch="/admin/blocked-ips/${item.getId()}/unblock"
                                                hx-confirm="차단을 해제하시겠습니까?"
                                                class="btn btn-ghost btn-xs text-success"
                                                title="차단 해제">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 10.5V6.75a4.5 4.5 0 1 1 9 0v3.75M3.75 21.75h10.5a2.25 2.25 0 0 0 2.25-2.25v-6.75a2.25 2.25 0 0 0-2.25-2.25H3.75a2.25 2.25 0 0 0-2.25 2.25v6.75a2.25 2.25 0 0 0 2.25 2.25Z"/>
                                            </svg>
                                        </button>
                                    @endif
                                    <button type="button"
                                            hx-get="/admin/blocked-ips/${item.getId()}/remove"
                                            hx-target="#HX_MODAL_CONTENT"
                                            class="btn btn-ghost btn-xs text-error"
                                            title="삭제">
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0"/>
                                        </svg>
                                    </button>
                                </div>
                            </td>
                        </tr>
                    @endfor
                `
            )

            @template.shared.layout.admin.pagination(
                totalCount = blockedIpData.getTotalCount(),
                currentPage = blockedIpData.getCurrentPage(),
                pageSize = blockedIpData.getPageSize(),
                totalPages = blockedIpData.getTotalPages(),
                baseUrl = "?search=" + req.getSearch()
            )
        </div>

    </div>
`)
