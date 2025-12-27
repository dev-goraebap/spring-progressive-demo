<%-- PWA 설치 배너 --%>
<div x-data x-show="$store.pwa.canInstall" x-cloak
     class="fixed bottom-20 sm:bottom-4 left-4 right-4 sm:left-auto sm:right-4 sm:w-80 z-[9999]
            bg-[#334155] text-white rounded-xl shadow-xl p-4">
    <div class="flex items-start gap-3">
        <img src="/images/logo.png" alt="데브고래밥" class="w-12 h-12 rounded-lg">
        <div class="flex-1">
            <p class="font-bold text-sm">앱 설치</p>
            <p class="text-xs opacity-80 mt-1">홈 화면에 추가하여 더 빠르게 접근하세요</p>
        </div>
        <button @click="$store.pwa.dismiss()" class="btn btn-ghost btn-xs btn-circle hover:bg-white/20">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
        </button>
    </div>
    <button @click="$store.pwa.install()" class="btn btn-sm w-full mt-3 bg-white text-[#334155] hover:bg-white/90 border-none">설치하기</button>
</div>
