import Alpine from 'alpinejs';

let deferredPrompt = null;

Alpine.store('pwa', {
    canInstall: false,

    install() {
        if (!deferredPrompt) return;

        deferredPrompt.prompt();
        deferredPrompt.userChoice.then((result) => {
            if (result.outcome === 'accepted') {
                this.canInstall = false;
            }
            deferredPrompt = null;
        });
    },

    dismiss() {
        this.canInstall = false;
        // 24시간 동안 다시 안 보기
        localStorage.setItem('pwa-dismiss', Date.now());
    }
});

window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;

    // 24시간 내 dismiss 했으면 안 보여줌
    const dismissed = localStorage.getItem('pwa-dismiss');
    if (dismissed && Date.now() - parseInt(dismissed) < 24 * 60 * 60 * 1000) {
        return;
    }

    Alpine.store('pwa').canInstall = true;
});

// 이미 설치된 경우
window.addEventListener('appinstalled', () => {
    Alpine.store('pwa').canInstall = false;
    deferredPrompt = null;
});
