<div x-data>
    <button @click="$store.theme.toggle()" x-cloak
            class="flex gap-4 items-center text-lg p-4 text-base-content">
        <div class="w-6"
             x-show="$store.theme.current === 'dark'">@template.shared.svg.sun()</div>
        <div class="w-6"
             x-show="$store.theme.current === 'cupcake'">@template.shared.svg.moon()</div>
    </button>
</div>