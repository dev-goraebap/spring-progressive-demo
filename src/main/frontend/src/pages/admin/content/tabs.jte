@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param String activeTab = "posts"

<div class="border-b border-base-300">
    <div class="px-4 lg:px-6 pt-4 lg:pt-6 pb-3 lg:pb-4">
        <h1 class="text-xl lg:text-2xl font-bold">블로그 콘텐츠</h1>
    </div>
    <div class="flex border-b border-base-300">
        <a href="/admin/posts"
           class="px-6 py-3 text-sm font-medium border-b-2 -mb-px ${activeTab.equals("posts") ? "border-primary text-primary" : "border-transparent hover:text-base-content/80"}">
            포스트
        </a>
        <a href="/admin/series"
           class="px-6 py-3 text-sm font-medium border-b-2 -mb-px ${activeTab.equals("series") ? "border-primary text-primary" : "border-transparent hover:text-base-content/80"}">
            시리즈
        </a>
        <a href="/admin/tags"
           class="px-6 py-3 text-sm font-medium border-b-2 -mb-px ${activeTab.equals("tags") ? "border-primary text-primary" : "border-transparent hover:text-base-content/80"}">
            태그
        </a>
    </div>
</div>
