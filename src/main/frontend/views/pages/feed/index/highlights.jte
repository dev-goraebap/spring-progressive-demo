@import xyz.goraebap.blog.infra.view_model.CurationViewModel
@import xyz.goraebap.blog.infra.view_model.PostViewModel
@import java.util.List

@param List<CurationViewModel> items
@param PostViewModel latestPatchNote = null

<div class="relative overflow-hidden bg-gradient-to-br from-primary/5 to-secondary/5">
    <%-- 파티클들 --%>
    <div class="absolute inset-0 overflow-hidden pointer-events-none">
        <div class="absolute w-2 h-2 bg-primary/20 rounded-full top-[10%] left-[15%] animate-pulse"></div>
        <div class="absolute w-1.5 h-1.5 bg-secondary/20 rounded-full top-[25%] right-[20%] animate-pulse [animation-delay:0.5s]"></div>
        <div class="absolute w-1 h-1 bg-primary/30 rounded-full top-[60%] left-[10%] animate-pulse [animation-delay:1s]"></div>
        <div class="absolute w-2 h-2 bg-secondary/15 rounded-full top-[70%] right-[15%] animate-pulse [animation-delay:0.3s]"></div>
        <div class="absolute w-1.5 h-1.5 bg-primary/25 rounded-full top-[40%] left-[80%] animate-pulse [animation-delay:0.7s]"></div>
        <div class="absolute w-1 h-1 bg-secondary/25 rounded-full top-[85%] left-[50%] animate-pulse [animation-delay:1.2s]"></div>
    </div>

    <div class="relative z-10 p-4 flex flex-col sm:flex-row gap-4">
        <%-- 큐레이션 뉴스 --%>
        <div class="flex-1">
            <div class="flex justify-between">
                <h2 class="font-bold mb-3">Today News</h2>
                <a href="/curations" class="text-sm hover:text-primary">더보기</a>
            </div>
            <div class="flex flex-col gap-2">
                @for(var item : items)
                    <a href="${item.getLink()}" target="_blank" class="flex flex-col hover:text-primary">
                        <p class="text-sm line-clamp-1">${item.getTitle()}</p>
                        <span class="text-xs text-base-content/70">${item.getSource()}</span>
                    </a>
                @endfor
            </div>
        </div>

        <%-- 최근 패치노트 --%>
        @if(latestPatchNote != null)
            <div class="sm:w-48 sm:border-l sm:border-base-300/50 sm:pl-4">
                <div class="flex justify-between">
                    <h2 class="font-bold mb-3 text-secondary">Patch Note</h2>
                    <a href="/patch-notes" class="text-sm hover:text-secondary">더보기</a>
                </div>
                <a href="/patch-notes/${latestPatchNote.getSlug()}" class="flex flex-col hover:text-secondary">
                    <p class="text-sm line-clamp-2">${latestPatchNote.getTitle()}</p>
                    <span class="text-xs text-base-content/70 mt-1 line-clamp-2">${latestPatchNote.getSummary()}</span>
                </a>
            </div>
        @endif
    </div>
</div>
