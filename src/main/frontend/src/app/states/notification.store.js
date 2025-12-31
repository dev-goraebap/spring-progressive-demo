import Alpine from 'alpinejs';
import { getFcmToken, registerFcmToken } from '@/shared/libs/firebase';

// iOS 감지
const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
const isStandalone = window.matchMedia('(display-mode: standalone)').matches
    || window.navigator.standalone === true;

Alpine.store('notification', {
    // 알림 권한 상태: 'default' | 'granted' | 'denied' | 'unsupported' | 'ios-browser'
    permission: 'default',

    async init() {
        this.updatePermission();

        // 권한이 granted면 토큰 상태 동기화
        if (this.permission === 'granted') {
            await this.syncToken();
        }
    },

    // 서버와 토큰 동기화 (조용히 처리)
    async syncToken() {
        try {
            const token = await getFcmToken();
            if (!token) return;

            // 서버에 등록 여부 확인
            const response = await fetch(`/api/fcm/check?token=${encodeURIComponent(token)}`);
            const isRegistered = await response.json();

            // 미등록이면 조용히 재등록
            if (!isRegistered) {
                await registerFcmToken(token);
            }
        } catch (error) {
            console.error('[Notification] 토큰 동기화 실패:', error);
        }
    },

    updatePermission() {
        if (!('Notification' in window)) {
            this.permission = 'unsupported';
            return;
        }

        // iOS 브라우저 (PWA 아닌 경우)
        if (isIOS && !isStandalone) {
            this.permission = 'ios-browser';
            return;
        }

        this.permission = Notification.permission;
    },

    // FCM 토큰 등록
    async registerToken() {
        const token = await getFcmToken();
        if (!token) {
            window.toast?.error('알림 설정에 실패했습니다.');
            return false;
        }

        const registered = await registerFcmToken(token);
        if (!registered) {
            window.toast?.error('알림 등록에 실패했습니다.');
            return false;
        }

        return true;
    }
});
