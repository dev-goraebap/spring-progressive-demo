@import xyz.goraebap.blog.app.admin.dto.AdminFcmIndexRequest
@import xyz.goraebap.blog.infra.view_model.FcmSubscriptionViewModel
@import xyz.goraebap.blog.infra.view_model.Pagination
@import java.time.format.DateTimeFormatter
@import java.util.Arrays
@import java.util.List
@import java.util.Map

@param Pagination<FcmSubscriptionViewModel> subscriptionData
@param AdminFcmIndexRequest req
@param int totalCount
@param List<Map<String, Object>> browserStats
@param List<Map<String, Object>> osStats
@param List<Map<String, Object>> deviceStats

!{var dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd HH:mm");}
!{List<String[]> tabs = Arrays.<String[]>asList(
    new String[]{"fcm", "푸시 알림", "/admin/fcm"},
    new String[]{"comments", "댓글", "/admin/comments"}
);}

@template.shared.layout.admin(title = "푸시 알림", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = "커뮤니티")
            @template.shared.layout.admin.tabs(tabs = tabs, activeTab = "fcm")
        </div>

        <%-- 통계 카드 --%>
        <div class="p-4 border-b border-base-300 bg-base-200/50">
            <div class="grid grid-cols-2 lg:grid-cols-4 gap-4">
                <%-- 총 구독자 --%>
                <div class="stat bg-base-100 rounded-lg shadow-sm p-4">
                    <div class="stat-title text-xs">총 구독자</div>
                    <div class="stat-value text-2xl">${totalCount}</div>
                </div>

                <%-- 브라우저별 --%>
                <div class="stat bg-base-100 rounded-lg shadow-sm p-4">
                    <div class="stat-title text-xs">브라우저</div>
                    <div class="text-sm mt-1">
                        @for(var stat : browserStats)
                            <div class="flex justify-between">
                                <span class="text-base-content/70">${String.valueOf(stat.get("name"))}</span>
                                <span class="font-medium">${String.valueOf(stat.get("count"))}</span>
                            </div>
                        @endfor
                        @if(browserStats.isEmpty())
                            <span class="text-base-content/50">-</span>
                        @endif
                    </div>
                </div>

                <%-- OS별 --%>
                <div class="stat bg-base-100 rounded-lg shadow-sm p-4">
                    <div class="stat-title text-xs">운영체제</div>
                    <div class="text-sm mt-1">
                        @for(var stat : osStats)
                            <div class="flex justify-between">
                                <span class="text-base-content/70">${String.valueOf(stat.get("name"))}</span>
                                <span class="font-medium">${String.valueOf(stat.get("count"))}</span>
                            </div>
                        @endfor
                        @if(osStats.isEmpty())
                            <span class="text-base-content/50">-</span>
                        @endif
                    </div>
                </div>

                <%-- 디바이스별 --%>
                <div class="stat bg-base-100 rounded-lg shadow-sm p-4">
                    <div class="stat-title text-xs">디바이스</div>
                    <div class="text-sm mt-1">
                        @for(var stat : deviceStats)
                            <div class="flex justify-between">
                                <span class="text-base-content/70">${String.valueOf(stat.get("name"))}</span>
                                <span class="font-medium">${String.valueOf(stat.get("count"))}</span>
                            </div>
                        @endfor
                        @if(deviceStats.isEmpty())
                            <span class="text-base-content/50">-</span>
                        @endif
                    </div>
                </div>
            </div>
        </div>

        @template.shared.layout.admin.filterForm(content = @`
            <input type="text"
                   name="search"
                   value="${req.getSearch()}"
                   placeholder="IP, 브라우저, OS 검색..."
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
                    hx-get="/admin/fcm/send"
                    hx-target="#HX_MODAL_CONTENT"
                    class="btn btn-primary btn-sm order-4">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"/>
                </svg>
                알림 발송
            </button>
        `)

        <div id="FCM_SUBSCRIPTION_TABLE">
            @template.shared.layout.admin.table(
                isEmpty = subscriptionData.getItems().isEmpty(),
                emptyMessage = "구독자가 없습니다.",
                head = @`
                    <th class="w-36">IP</th>
                    <th class="w-28">브라우저</th>
                    <th class="w-28">OS</th>
                    <th class="w-24">디바이스</th>
                    <th class="min-w-48">토큰</th>
                    <th class="w-28">구독일</th>
                `,
                body = @`
                    @for(var item : subscriptionData.getItems())
                        <tr class="hover:bg-base-200">
                            <td>
                                <code class="text-sm">${item.getIpAddress() != null ? item.getIpAddress() : "-"}</code>
                            </td>
                            <td class="text-sm">
                                ${item.getBrowser() != null ? item.getBrowser() : "-"}
                            </td>
                            <td class="text-sm">
                                ${item.getOs() != null ? item.getOs() : "-"}
                            </td>
                            <td>
                                <span class="badge badge-sm badge-ghost">
                                    ${item.getDeviceTypeLabel()}
                                </span>
                            </td>
                            <td>
                                <code class="text-xs text-base-content/70" title="${item.getToken()}">
                                    ${item.getTokenPreview()}
                                </code>
                            </td>
                            <td class="text-sm text-base-content/70">
                                ${item.getCreatedAt().format(dateTimeFormat)}
                            </td>
                        </tr>
                    @endfor
                `
            )

            @template.shared.layout.admin.pagination(
                totalCount = subscriptionData.getTotalCount(),
                currentPage = subscriptionData.getCurrentPage(),
                pageSize = subscriptionData.getPageSize(),
                totalPages = subscriptionData.getTotalPages(),
                baseUrl = "?search=" + req.getSearch()
            )
        </div>

    </div>
`)
