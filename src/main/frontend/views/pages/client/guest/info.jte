@param String permission

<div class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = "방문자 정보")

    <div class="p-4 flex-1 overflow-y-auto space-y-6">
        <%-- 인사말 --%>
        <div>
            <h3 class="text-lg font-bold">안녕하세요, 방문자님</h3>
            <p class="text-sm text-base-content/60 mt-1">데브고래밥 블로그에 오신 것을 환영합니다</p>
        </div>

        <%-- 알림 구독 섹션 --%>
        <div class="bg-base-200 rounded-xl p-4">
            <div class="flex items-center gap-3 mb-3">
                <div class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center">
                    <svg class="w-5 h-5 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"/>
                    </svg>
                </div>
                <div>
                    <h4 class="font-bold">알림 구독</h4>
                    <p class="text-xs text-base-content/60">새로운 글 소식을 받아보세요</p>
                </div>
            </div>

            @if(permission.equals("granted"))
                <%-- 구독 완료 상태 --%>
                <div class="flex items-center gap-2 text-success">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    <span class="text-sm font-medium">알림이 설정되어 있습니다</span>
                </div>
                <p class="text-xs text-base-content/60 mt-2">새로운 글이 올라오면 알려드릴게요!</p>
            @elseif(permission.equals("denied"))
                <%-- 차단 상태 --%>
                <div class="flex items-center gap-2 text-error">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636"/>
                    </svg>
                    <span class="text-sm font-medium">알림이 차단되어 있습니다</span>
                </div>
                <p class="text-xs text-base-content/60 mt-2">
                    알림을 받으시려면 브라우저 설정에서 이 사이트의 알림을 허용해주세요.
                </p>
                <div class="mt-3 p-3 bg-base-100 rounded-lg text-xs text-base-content/70">
                    <p class="font-medium mb-1">설정 방법:</p>
                    <p>주소창 왼쪽의 자물쇠/정보 아이콘 클릭 → 알림 → 허용</p>
                </div>
            @elseif(permission.equals("ios-browser"))
                <%-- iOS 브라우저 상태 --%>
                <div class="flex items-center gap-2 text-warning">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <span class="text-sm font-medium">iOS에서는 앱 설치가 필요해요</span>
                </div>
                <p class="text-xs text-base-content/60 mt-2">
                    Safari에서 공유 버튼 → '홈 화면에 추가'를 눌러 앱으로 설치하면 알림을 받을 수 있어요.
                </p>
            @elseif(permission.equals("unsupported"))
                <%-- 미지원 브라우저 --%>
                <div class="flex items-center gap-2 text-base-content/60">
                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    <span class="text-sm font-medium">이 브라우저는 알림을 지원하지 않습니다</span>
                </div>
            @else
                <%-- 기본 상태 (default) - 구독 유도 --%>
                <p class="text-sm text-base-content/80 mb-3">
                    구독하시면 새로운 글이 올라올 때 알림을 보내드려요. 언제든지 해제할 수 있어요.
                </p>
                <button type="button"
                        class="btn btn-primary btn-sm w-full"
                        x-data="notificationSubscribe"
                        :disabled="loading"
                        @click="subscribe()">
                    <span x-show="loading" class="loading loading-spinner loading-sm"></span>
                    <span x-show="!loading">알림 구독하기</span>
                </button>
            @endif
        </div>

        <%-- PWA 섹션 (추후 추가) --%>
        <%-- <div class="bg-base-200 rounded-xl p-4">
            ...
        </div> --%>
    </div>
</div>
