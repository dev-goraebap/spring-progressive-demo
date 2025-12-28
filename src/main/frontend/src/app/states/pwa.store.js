import Alpine from 'alpinejs';
import { getFcmToken, registerFcmToken } from '@/shared/libs/firebase';

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

// 설치 완료 → 알림 권한 요청 → FCM 토큰 등록
window.addEventListener('appinstalled', async () => {
    Alpine.store('pwa').available = false;
    deferredPrompt = null;

    window.toast?.success('앱이 설치되었습니다!');

    // 알림 권한 요청 (약간의 딜레이 후)
    setTimeout(async () => {
        if (!('Notification' in window)) return;

        let permission = Notification.permission;
        if (permission === 'default') {
            permission = await Notification.requestPermission();
        }

        if (permission === 'granted') {
            // FCM 토큰 발급 및 서버 등록
            const token = await getFcmToken();
            if (token) {
                const registered = await registerFcmToken(token);
                if (registered) {
                    window.toast?.success('새 글 알림을 받을 수 있습니다!');
                }
            }
        }
    }, 1500);
});
