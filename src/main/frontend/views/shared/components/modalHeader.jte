@param String title

<div class="flex items-center gap-2 p-4 border-b border-base-300 shrink-0">
    <button type="button" @click="$store.modal.onClose()" class="btn btn-ghost btn-sm btn-circle">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
    </button>
    <h2 class="text-lg font-bold">${title}</h2>
</div>
