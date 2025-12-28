import Alpine from 'alpinejs';
import { getFcmToken, registerFcmToken } from '@/shared/libs/firebase';

let deferredPrompt = null;

// PWA standalone 모드인지 확인
const isStandalone = window.matchMedia('(display-mode: standalone)').matches
    || window.navigator.standalone === true;

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

// 설치 완료 → 토스트만 표시 (토큰 발급은 PWA 실행 시)
window.addEventListener('appinstalled', () => {
    Alpine.store('pwa').available = false;
    deferredPrompt = null;
    window.toast?.success('앱이 설치되었습니다!');
});

// PWA 모드로 실행 시 → 알림 권한 요청 → FCM 토큰 등록
if (isStandalone) {
    (async () => {
        if (!('Notification' in window)) return;

        let permission = Notification.permission;
        if (permission === 'default') {
            permission = await Notification.requestPermission();
        }
        if (permission !== 'granted') return;

        const token = await getFcmToken();
        if (!token) return;

        // 이미 등록된 토큰이면 스킵
        const savedToken = localStorage.getItem('fcm_token');
        if (savedToken === token) return;

        const registered = await registerFcmToken(token);
        if (!registered) return;

        localStorage.setItem('fcm_token', token);
        window.toast?.info('알림이 활성화되었습니다.');
    })();
}
