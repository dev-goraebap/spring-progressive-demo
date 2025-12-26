@param String title

<div class="px-4 lg:px-6 pt-4 lg:pt-6 pb-3 lg:pb-4 flex justify-between items-center">
    <h1 class="text-xl lg:text-2xl font-bold">${title}</h1>

    <%-- 테마 토글 (상단 좌측 고정) --%>
    <div class="z-10">
        @template.shared.components.themeToggle(isAdminPage = true)
    </div>
</div>
