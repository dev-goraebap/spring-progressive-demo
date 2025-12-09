@template.shared.layout.admin(title = "태그 관리", content = @`

@template.pages.admin.content.tabs(activeTab = "tags")

<div class="card bg-base-200">
    <div class="card-body">
        <div class="flex justify-between items-center mb-4">
            <h2 class="card-title">태그 목록</h2>
            <a href="/admin/tags/new" class="btn btn-primary btn-sm">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                </svg>
                새 태그
            </a>
        </div>

        <p class="text-base-content/60">태그가 없습니다.</p>
    </div>
</div>

`)
