@param int totalCount
@param int currentPage
@param int pageSize
@param int totalPages
@param String baseUrl

!{var sizes = new int[]{10, 30, 50};}
!{var siblingCount = 1;}
!{var showLeftEllipsis = currentPage > siblingCount + 2;}
!{var showRightEllipsis = currentPage < totalPages - siblingCount - 1;}
!{var leftStart = Math.max(2, currentPage - siblingCount);}
!{var rightEnd = Math.min(totalPages - 1, currentPage + siblingCount);}

<div class="flex flex-col lg:flex-row items-center justify-between gap-4 px-4 lg:px-6 py-4 border-t border-base-300">
    <span class="text-sm text-base-content/70">${totalCount}개의 콘텐츠</span>
    <div class="flex flex-col lg:flex-row items-center gap-4 lg:gap-6">
        <div class="flex items-center gap-2">
            <span class="text-sm text-base-content/70">페이지당 행 수</span>
            <select class="select select-bordered select-sm w-20"
                    onchange="location.href='${baseUrl}&page=1&size=' + this.value">
                @for(var size : sizes)
                    @if(pageSize == size)
                        <option value="${size}" selected>${size}</option>
                    @else
                        <option value="${size}">${size}</option>
                    @endif
                @endfor
            </select>
        </div>
        @if(totalPages > 0)
            <div class="join">
                <%-- 이전 버튼 --%>
                @if(currentPage > 1)
                    <a href="${baseUrl}&page=${currentPage - 1}&size=${pageSize}"
                       class="join-item btn btn-sm">«</a>
                @else
                    <button class="join-item btn btn-sm btn-disabled">«</button>
                @endif

                <%-- 첫 페이지 --%>
                @if(currentPage == 1)
                    <button class="join-item btn btn-sm btn-primary">1</button>
                @else
                    <a href="${baseUrl}&page=1&size=${pageSize}"
                       class="join-item btn btn-sm">1</a>
                @endif

                <%-- 왼쪽 생략 --%>
                @if(showLeftEllipsis)
                    <span class="join-item btn btn-sm pointer-events-none">...</span>
                @endif

                <%-- 중간 페이지들 --%>
                @for(int i = leftStart; i <= rightEnd; i++)
                    @if(i > 1 && i < totalPages)
                        @if(i == currentPage)
                            <button class="join-item btn btn-sm btn-primary">${i}</button>
                        @else
                            <a href="${baseUrl}&page=${i}&size=${pageSize}"
                               class="join-item btn btn-sm">${i}</a>
                        @endif
                    @endif
                @endfor

                <%-- 오른쪽 생략 --%>
                @if(showRightEllipsis)
                    <span class="join-item btn btn-sm pointer-events-none">...</span>
                @endif

                <%-- 마지막 페이지 --%>
                @if(totalPages > 1)
                    @if(currentPage == totalPages)
                        <button class="join-item btn btn-sm btn-primary">${totalPages}</button>
                    @else
                        <a href="${baseUrl}&page=${totalPages}&size=${pageSize}"
                           class="join-item btn btn-sm">${totalPages}</a>
                    @endif
                @endif

                <%-- 다음 버튼 --%>
                @if(currentPage < totalPages)
                    <a href="${baseUrl}&page=${currentPage + 1}&size=${pageSize}"
                       class="join-item btn btn-sm">»</a>
                @else
                    <button class="join-item btn btn-sm btn-disabled">»</button>
                @endif
            </div>
        @endif
    </div>
</div>
