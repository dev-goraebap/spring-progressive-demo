@import gg.jte.Content

@param String title = "콘텐츠"
@param String description = null
@param boolean backAction = false
@param String backUrl = null
@param Content right = null

<div class="w-full p-4 border-b border-base-300 flex ${right != null ? "justify-between" : "items-center gap-2"} sticky top-0 bg-base-100 z-[999]">
    <%-- 왼쪽 영역 --%>
    @if(backAction)
        @if(backUrl != null)
            <a href="${backUrl}" class="flex items-center gap-1">
                <div class="w-5">@template.shared.svg.back()</div>
                <span>${title}</span>
            </a>
        @else
            <button onclick="history.back()" class="flex items-center gap-1">
                <div class="w-5">@template.shared.svg.back()</div>
                <span>${title}</span>
            </button>
        @endif
    @else
        <div>
            <h1 class="font-bold">${title}</h1>
            @if(description != null)
                <p class="text-xs hidden sm:block">${description}</p>
            @endif
        </div>
    @endif

    <%-- 오른쪽 영역 --%>
    @if(right != null)
        ${right}
    @endif
</div>
