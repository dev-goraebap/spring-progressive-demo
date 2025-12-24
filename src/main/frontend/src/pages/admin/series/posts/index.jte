@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminSeriesViewModel
@import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesPostViewModel
@import java.util.List

@param AdminSeriesViewModel series
@param List<SeriesPostViewModel> posts

@template.shared.layout.admin(title = "시리즈 포스트 관리", content = @`
    <div class="flex flex-col h-full">

        <div class="border-b border-base-300">
            @template.shared.layout.admin.header(title = series.getName() + " - 포스트 관리")
        </div>

        <div class="p-4 border-b border-base-300 flex items-center gap-2 flex-wrap">
            <a href="/admin/series" class="btn btn-ghost btn-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M10.5 19.5 3 12m0 0 7.5-7.5M3 12h18"/>
                </svg>
                시리즈 목록
            </a>
            <div class="flex-1"></div>
            <button type="button"
                    hx-get="/admin/series/${series.getId()}/posts/add"
                    hx-target="#HX_MODAL_CONTENT"
                    class="btn btn-primary btn-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                포스트 추가
            </button>
        </div>

        <div class="flex-1 overflow-auto p-4" x-data="seriesPostsManager">

            @if(posts.isEmpty())
                <div class="flex flex-col items-center justify-center h-64 text-base-content/60">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-12 mb-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"/>
                    </svg>
                    <p class="text-lg font-medium">포스트가 없습니다</p>
                    <p class="text-sm">포스트를 추가해주세요</p>
                </div>
            @else
                <form class="flex justify-end mb-4"
                      hx-put="/admin/series/${series.getId()}/posts/orders"
                      hx-swap="none"
                      @htmx:after-request="onOrdersSaved">
                    <input type="hidden" name="orders" x-ref="ordersInput" />
                    <button type="submit"
                            x-show="changed"
                            x-cloak
                            class="btn btn-success btn-sm group"
                            hx-disabled-elt="this">
                        <span class="loading loading-spinner loading-xs hidden group-[.htmx-request]:inline-flex"></span>
                        순서 저장
                    </button>
                </form>

                <ul x-ref="sortableList" class="space-y-2">
                    @for(var post : posts)
                        <li data-id="${post.getId()}"
                            class="flex items-center gap-3 p-3 bg-base-100 border border-base-300 rounded-lg hover:shadow-sm">
                            <div class="drag-handle cursor-move text-base-content/40 hover:text-base-content">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-5">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"/>
                                </svg>
                            </div>

                            @if(post.getThumbnailUrl() != null)
                                <div class="w-16 h-10 rounded overflow-hidden flex-shrink-0">
                                    @template.shared.components.img(
                                        url = post.getThumbnailUrl(),
                                        dominantColor = post.getThumbnailDominantColor(),
                                        alt = post.getPostTitle()
                                    )
                                </div>
                            @else
                                <div class="w-16 h-10 rounded bg-base-300 flex items-center justify-center flex-shrink-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4 text-base-content/40">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="m2.25 15.75 5.159-5.159a2.25 2.25 0 0 1 3.182 0l5.159 5.159m-1.5-1.5 1.409-1.409a2.25 2.25 0 0 1 3.182 0l2.909 2.909m-18 3.75h16.5a1.5 1.5 0 0 0 1.5-1.5V6a1.5 1.5 0 0 0-1.5-1.5H3.75A1.5 1.5 0 0 0 2.25 6v12a1.5 1.5 0 0 0 1.5 1.5Zm10.5-11.25h.008v.008h-.008V8.25Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"/>
                                    </svg>
                                </div>
                            @endif

                            <div class="flex-1 min-w-0">
                                <div class="font-medium truncate">${post.getPostTitle()}</div>
                                <div class="text-xs text-base-content/60">${post.getPostSlug()}</div>
                            </div>

                            <button type="button"
                                    hx-delete="/admin/series/${series.getId()}/posts/${post.getPostId()}"
                                    hx-confirm="정말 이 포스트를 시리즈에서 제거하시겠습니까?"
                                    class="btn btn-ghost btn-sm text-error">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12"/>
                                </svg>
                            </button>
                        </li>
                    @endfor
                </ul>
            @endif
        </div>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
`)
