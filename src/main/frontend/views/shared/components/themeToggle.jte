@param Boolean isAdminPage = false

<div x-data>
    <button @click="$store.theme.toggle()" x-cloak
            class="flex gap-4 items-center text-lg text-base-content cursor-pointer ${isAdminPage ? "" : "p-4"}">
        <div class="w-6"
             x-show="$store.theme.current === 'dark'">@template.shared.svg.sun()</div>
        <div class="w-6"
             x-show="$store.theme.current === 'light'">@template.shared.svg.moon()</div>
    </button>
</div>