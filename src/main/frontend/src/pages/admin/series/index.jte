@template.shared.layout.admin(title = "시리즈 관리", content = @`

@template.pages.admin.content.tabs(activeTab = "series")

<div class="card bg-base-200">
    <div class="card-body">
        <div class="flex justify-between items-center mb-4">
            <h2 class="card-title">시리즈 목록</h2>
            <a href="/admin/series/new" class="btn btn-primary btn-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                새 시리즈
            </a>
        </div>

        <p class="text-base-content/60">시리즈가 없습니다.</p>
    </div>
</div>

`)
