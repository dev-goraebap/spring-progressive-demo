import Alpine from 'alpinejs';

let deferredPrompt = null;

// PWA standalone 모드인지 확인
const isStandalone = window.matchMedia('(display-mode: standalone)').matches
    || window.navigator.standalone === true;

// iOS Safari 감지
const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
const isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
const isIOSSafari = isIOS && isSafari;

Alpine.store('pwa', {
    // 설치 가능 여부 (beforeinstallprompt 이벤트 발생 시 true)
    available: false,
    // iOS Safari 여부
    isIOSSafari: isIOSSafari,
    // 이미 설치됨 (standalone 모드)
    isInstalled: isStandalone,

    // 설치 실행
    install() {
        // 이미 설치됨
        if (isStandalone) {
            window.toast?.info('이미 설치되어 있습니다. 홈 화면에서 앱을 실행해주세요!');
            return;
        }

        // iOS Safari: 수동 설치 안내
        if (isIOSSafari) {
            window.toast?.info('하단의 공유 버튼 → "홈 화면에 추가"를 눌러주세요!');
            return;
        }

        // Chrome 등: 자동 설치 프롬프트
        if (deferredPrompt) {
            deferredPrompt.prompt();
            deferredPrompt.userChoice.then(() => {
                deferredPrompt = null;
                this.available = false;
            });
            return;
        }

        // 설치 불가능한 환경
        window.toast?.info('이 브라우저에서는 앱 설치를 지원하지 않습니다.');
    }
});

// 설치 가능 이벤트 - deferredPrompt 저장
window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    Alpine.store('pwa').available = true;
});

// 설치 완료 → 토스트
window.addEventListener('appinstalled', () => {
    Alpine.store('pwa').available = false;
    deferredPrompt = null;
    window.toast?.success('앱이 설치되었습니다!');
});
