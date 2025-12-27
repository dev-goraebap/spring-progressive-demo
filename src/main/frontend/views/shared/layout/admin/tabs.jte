@param java.util.List<String[]> tabs
@param String activeTab

<div class="flex border-b border-base-300">
    @for(var tab : tabs)
        !{var tabId = tab[0];}
        !{var tabLabel = tab[1];}
        !{var tabHref = tab[2];}
        <a href="${tabHref}"
           class="px-6 py-3 text-sm font-medium border-b-2 -mb-px ${activeTab.equals(tabId) ? "border-primary text-primary" : "border-transparent hover:text-base-content/80"}">
            ${tabLabel}
        </a>
    @endfor
</div>
