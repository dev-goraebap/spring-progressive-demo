import Alpine from 'alpinejs';
import { getFcmToken, registerFcmToken } from '@/shared/libs/firebase';

// iOS 감지
const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
const isStandalone = window.matchMedia('(display-mode: standalone)').matches
    || window.navigator.standalone === true;

Alpine.store('notification', {
    // 알림 권한 상태: 'default' | 'granted' | 'denied' | 'unsupported' | 'ios-browser'
    permission: 'default',
    // 모달 표시 여부
    showModal: false,
    // 모달 타입: 'explain' | 'denied' | 'ios'
    modalType: 'explain',
    // 로딩 상태
    loading: false,

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

    // 배너 클릭 시 호출
    async handleClick() {
        this.updatePermission();

        switch (this.permission) {
            case 'granted':
                await this.ensureTokenRegistered();
                break;

            case 'default':
                this.modalType = 'explain';
                this.showModal = true;
                break;

            case 'denied':
                this.modalType = 'denied';
                this.showModal = true;
                break;

            case 'ios-browser':
                this.modalType = 'ios';
                this.showModal = true;
                break;

            case 'unsupported':
                window.toast?.error('이 브라우저는 알림을 지원하지 않습니다.');
                break;
        }
    },

    // 토큰 등록 확인 및 처리
    async ensureTokenRegistered() {
        try {
            const token = await getFcmToken();
            if (!token) {
                window.toast?.error('토큰을 가져올 수 없습니다.');
                return false;
            }

            // 서버에 등록 여부 확인
            const response = await fetch(`/api/fcm/check?token=${encodeURIComponent(token)}`);
            const isRegistered = await response.json();

            if (isRegistered) {
                window.toast?.info('알림이 이미 설정되어 있습니다.');
                return true;
            }

            // 미등록이면 등록 시도
            return await this.registerToken();
        } catch (error) {
            console.error('[Notification] 토큰 확인 실패:', error);
            return false;
        }
    },

    // 모달에서 확인 클릭 시
    async onConfirm() {
        if (this.modalType !== 'explain') {
            this.showModal = false;
            return;
        }

        this.loading = true;

        try {
            // 권한 요청
            const permission = await Notification.requestPermission();
            this.permission = permission;

            if (permission !== 'granted') {
                window.toast?.warning('알림 권한이 거부되었습니다.');
                this.showModal = false;
                return;
            }

            // FCM 토큰 등록
            const success = await this.registerToken();
            if (success) {
                window.toast?.success('알림이 설정되었습니다!');
            }
        } finally {
            this.loading = false;
            this.showModal = false;
        }
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
    },

    // 모달 닫기
    closeModal() {
        this.showModal = false;
    }
});
