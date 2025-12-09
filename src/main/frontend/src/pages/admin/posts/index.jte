@template.shared.layout.admin(title = "포스트 관리", content = @`
<div class="flex flex-col h-full">

@template.pages.admin.content.tabs(activeTab = "posts")

<%-- 필터 폼 --%>
<div class="flex flex-wrap items-center gap-2 lg:gap-4 px-4 lg:px-6 py-4 border-b border-base-300">
    <input type="text"
           placeholder="제목 검색..."
           class="input input-bordered input-sm w-full lg:w-64 order-1"/>
    <select class="select select-bordered select-sm flex-1 lg:flex-none lg:w-28 order-2">
        <option>전체 타입</option>
        <option>일반</option>
        <option>시리즈</option>
    </select>
    <select class="select select-bordered select-sm flex-1 lg:flex-none lg:w-24 order-3">
        <option>전체</option>
        <option>공개</option>
        <option>비공개</option>
    </select>
    <div class="hidden lg:block flex-1 order-4"></div>
    <a href="/admin/posts/new" class="btn btn-primary btn-sm w-full lg:w-auto order-5">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
        </svg>
        새 포스트
    </a>
</div>

<%-- 테이블 --%>
<div class="flex-1 overflow-x-auto">
    <table class="table table-sm lg:table-md whitespace-nowrap">
        <thead>
            <tr class="border-b border-base-300">
                <th class="w-16 lg:w-20">썸네일</th>
                <th class="min-w-40">제목</th>
                <th class="w-20 lg:w-24">타입</th>
                <th class="w-28 lg:w-32">태그</th>
                <th class="w-16 lg:w-20">상태</th>
                <th class="w-16 lg:w-20">조회수</th>
                <th class="w-24 lg:w-28">발행일</th>
                <th class="w-24 lg:w-28">생성일</th>
                <th class="w-16 lg:w-20">관리</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td colspan="9" class="text-center py-12 text-base-content/60">
                    포스트가 없습니다.
                </td>
            </tr>
        </tbody>
    </table>
</div>

<%-- 페이징 --%>
<div class="flex flex-col lg:flex-row items-center justify-between gap-4 px-4 lg:px-6 py-4 border-t border-base-300">
    <span class="text-sm text-base-content/70">33개의 콘텐츠</span>
    <div class="flex flex-col lg:flex-row items-center gap-4 lg:gap-6">
        <div class="flex items-center gap-2">
            <span class="text-sm text-base-content/70">페이지당 행 수</span>
            <select class="select select-bordered select-sm w-20">
                <option>10</option>
                <option>30</option>
                <option>50</option>
            </select>
        </div>
        <div class="join">
            <button class="join-item btn btn-sm btn-disabled">«</button>
            <button class="join-item btn btn-sm btn-active">1</button>
            <button class="join-item btn btn-sm">2</button>
            <button class="join-item btn btn-sm">3</button>
            <button class="join-item btn btn-sm">»</button>
        </div>
    </div>
</div>

</div>
`)
