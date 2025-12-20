<%-- 로딩 템플릿 --%>
<template id="modal-loading">
    <div class="flex items-center justify-center py-20">
        <span class="loading loading-spinner loading-lg"></span>
    </div>
</template>

<%-- 모달 --%>
<div x-data="{ show: false }"
     x-init="$watch('$store.modal.isOpen', value => show = value)"
     x-cloak>

    <%-- 오버레이 --%>
    <div x-show="show"
         x-transition:enter="transition-opacity ease-out duration-300"
         x-transition:enter-start="opacity-0"
         x-transition:enter-end="opacity-100"
         x-transition:leave="transition-opacity ease-in duration-200"
         x-transition:leave-start="opacity-100"
         x-transition:leave-end="opacity-0"
         @click="$store.modal.closeable && $store.modal.onClose()"
         class="fixed inset-0 bg-black/50 z-[9998]">
    </div>

    <%-- 모달 컨테이너 --%>
    <div x-show="show"
         x-transition:enter="transition ease-out duration-300"
         x-transition:enter-start="opacity-0 scale-95"
         x-transition:enter-end="opacity-100 scale-100"
         x-transition:leave="transition ease-in duration-200"
         x-transition:leave-start="opacity-100 scale-100"
         x-transition:leave-end="opacity-0 scale-95"
         class="fixed inset-0 sm:inset-auto sm:top-1/2 sm:left-1/2 sm:-translate-x-1/2 sm:-translate-y-1/2 sm:w-full sm:max-w-lg sm:max-h-[90vh] sm:rounded-xl bg-base-100 flex flex-col overflow-hidden z-[9999]">

        <%-- 콘텐츠 영역 --%>
        <div id="modal-content" class="flex-1 flex flex-col overflow-hidden"></div>
    </div>
</div>
