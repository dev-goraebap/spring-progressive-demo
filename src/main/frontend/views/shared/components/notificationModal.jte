<%-- 알림 설정 모달 --%>
<div x-show="$store.notification.showModal"
     x-cloak
     class="fixed inset-0 z-[9999]">

    <%-- 오버레이 --%>
    <div x-show="$store.notification.showModal"
         x-transition:enter="transition-opacity ease-out duration-200"
         x-transition:enter-start="opacity-0"
         x-transition:enter-end="opacity-100"
         x-transition:leave="transition-opacity ease-in duration-150"
         x-transition:leave-start="opacity-100"
         x-transition:leave-end="opacity-0"
         @click="$store.notification.closeModal()"
         class="absolute inset-0 bg-black/50">
    </div>

    <%-- 모달 컨텐츠 --%>
    <div class="absolute inset-0 flex items-center justify-center p-4 pointer-events-none">
        <div x-show="$store.notification.showModal"
             x-transition:enter="transition ease-out duration-200"
             x-transition:enter-start="opacity-0 scale-95"
             x-transition:enter-end="opacity-100 scale-100"
             x-transition:leave="transition ease-in duration-150"
             x-transition:leave-start="opacity-100 scale-100"
             x-transition:leave-end="opacity-0 scale-95"
             class="w-full max-w-sm bg-base-100 rounded-2xl shadow-xl pointer-events-auto overflow-hidden">

            <%-- 설명 모달 (default 상태) --%>
            <template x-if="$store.notification.modalType === 'explain'">
                <div class="p-6">
                    <div class="text-center mb-4">
                        <div class="w-16 h-16 bg-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
                            </svg>
                        </div>
                        <h3 class="text-lg font-bold">새 글 알림 받기</h3>
                        <p class="text-sm text-base-content/70 mt-2">
                            새로운 글이 올라오면 알림을 보내드려요.<br>
                            확인을 누르면 브라우저에서 알림 허용 팝업이 표시됩니다.
                        </p>
                    </div>
                    <div class="flex gap-2">
                        <button @click="$store.notification.closeModal()"
                                class="btn btn-ghost flex-1">
                            취소
                        </button>
                        <button @click="$store.notification.onConfirm()"
                                :disabled="$store.notification.loading"
                                class="btn btn-primary flex-1">
                            <span x-show="$store.notification.loading" class="loading loading-spinner loading-sm"></span>
                            <span x-show="!$store.notification.loading">확인</span>
                        </button>
                    </div>
                </div>
            </template>

            <%-- 차단됨 안내 모달 --%>
            <template x-if="$store.notification.modalType === 'denied'">
                <div class="p-6">
                    <div class="text-center mb-4">
                        <div class="w-16 h-16 bg-warning/10 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg class="w-8 h-8 text-warning" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                            </svg>
                        </div>
                        <h3 class="text-lg font-bold">알림이 차단되어 있습니다</h3>
                        <p class="text-sm text-base-content/70 mt-2">
                            알림을 받으려면 브라우저 설정에서<br>
                            직접 허용해주셔야 합니다.
                        </p>
                    </div>
                    <div class="bg-base-200 rounded-lg p-4 mb-4">
                        <p class="text-sm">
                            <span class="font-semibold">설정 방법:</span><br>
                            주소창 왼쪽 <span class="badge badge-sm">🔒</span> 아이콘 클릭<br>
                            → 알림 → <span class="text-primary font-medium">허용</span>으로 변경
                        </p>
                    </div>
                    <button @click="$store.notification.closeModal()"
                            class="btn btn-primary w-full">
                        확인
                    </button>
                </div>
            </template>

            <%-- iOS 브라우저 안내 모달 --%>
            <template x-if="$store.notification.modalType === 'ios'">
                <div class="p-6">
                    <div class="text-center mb-4">
                        <div class="w-16 h-16 bg-info/10 rounded-full flex items-center justify-center mx-auto mb-4">
                            <svg class="w-8 h-8 text-info" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"/>
                            </svg>
                        </div>
                        <h3 class="text-lg font-bold">앱 설치가 필요합니다</h3>
                        <p class="text-sm text-base-content/70 mt-2">
                            iOS에서는 앱을 설치해야<br>
                            알림을 받을 수 있습니다.
                        </p>
                    </div>
                    <div class="bg-base-200 rounded-lg p-4 mb-4">
                        <p class="text-sm">
                            <span class="font-semibold">설치 방법:</span><br>
                            하단 공유 버튼 <span class="badge badge-sm">⬆️</span> 클릭<br>
                            → <span class="text-primary font-medium">홈 화면에 추가</span> 선택
                        </p>
                    </div>
                    <button @click="$store.notification.closeModal()"
                            class="btn btn-primary w-full">
                        확인
                    </button>
                </div>
            </template>
        </div>
    </div>
</div>
