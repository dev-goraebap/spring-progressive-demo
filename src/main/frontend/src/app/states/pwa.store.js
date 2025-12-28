import Alpine from 'alpinejs';

let deferredPrompt = null;

Alpine.store('pwa', {
    // 설치 가능 여부 (beforeinstallprompt 이벤트 발생 시 true)
    available: false,

    // 설치 실행
    install() {
        if (!deferredPrompt) return;

        deferredPrompt.prompt();
        deferredPrompt.userChoice.then(() => {
            deferredPrompt = null;
            this.available = false;
        });
    }
});

// 설치 가능 이벤트 - deferredPrompt 저장
window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    Alpine.store('pwa').available = true;
});

// 설치 완료 → 알림 권한 요청
window.addEventListener('appinstalled', async () => {
    Alpine.store('pwa').available = false;
    Alpine.store('pwa').showPrompt = false;
    deferredPrompt = null;

    window.toast?.success('앱이 설치되었습니다!');

    // 알림 권한 요청 (약간의 딜레이 후)
    setTimeout(async () => {
        if (!('Notification' in window)) return;
        if (Notification.permission === 'granted') return;

        const permission = await Notification.requestPermission();
        if (permission === 'granted') {
            window.toast?.success('알림을 받을 수 있습니다!');
            // TODO: FCM 토큰 등록 API 호출
        }
    }, 1500);
});
