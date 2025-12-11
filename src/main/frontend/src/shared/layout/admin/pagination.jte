@param int totalCount
@param int currentPage
@param int pageSize
@param int totalPages
@param String baseUrl

!{var sizes = new int[]{10, 30, 50};}

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
                @if(currentPage > 1)
                    <a href="${baseUrl}&page=${currentPage - 1}&size=${pageSize}"
                       class="join-item btn btn-sm">«</a>
                @else
                    <button class="join-item btn btn-sm btn-disabled">«</button>
                @endif

                @for(int i = 1; i <= totalPages; i++)
                    @if(i == currentPage)
                        <button class="join-item btn btn-sm btn-primary">${i}</button>
                    @else
                        <a href="${baseUrl}&page=${i}&size=${pageSize}"
                           class="join-item btn btn-sm">${i}</a>
                    @endif
                @endfor

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
