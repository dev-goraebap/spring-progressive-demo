import { Notyf } from 'notyf';

let notyf = null;
const TOAST_KEY = 'pendingToast';

const getNotyf = () => {
    // 컨테이너가 DOM에 없으면 새 인스턴스 생성
    if (!notyf || !document.body.contains(document.querySelector('.notyf'))) {
        notyf = new Notyf({
            duration: 3000,
            position: { x: 'right', y: 'top' },
            dismissible: true,
            types: [
                {
                    type: 'warning',
                    background: '#f59e0b',
                    icon: false
                },
                {
                    type: 'info',
                    background: '#3b82f6',
                    icon: false
                }
            ]
        });
    }
    return notyf;
};

/**
 * 페이지 로드 시 pending toast 표시
 */
const showPendingToast = () => {
    const pending = sessionStorage.getItem(TOAST_KEY);
    if (pending) {
        sessionStorage.removeItem(TOAST_KEY);
        const { type, message } = JSON.parse(pending);
        toast[type](message);
    }
};

const toast = {
    success(message) {
        getNotyf().success(message);
    },
    warning(message) {
        getNotyf().open({ type: 'warning', message });
    },
    info(message) {
        getNotyf().open({ type: 'info', message });
    },
    error(message) {
        getNotyf().error(message);
    },
    /**
     * htmx 페이지 스왑 완료 후 토스트 표시
     * body outerHTML 스왑 시 JS 컨텍스트가 초기화되므로 document 이벤트 사용
     */
    defer(type, message) {
        document.addEventListener('htmx:afterSettle', () => {
            this[type](message);
        }, { once: true });
    },
    /**
     * 페이지 이동 후 토스트 표시 (sessionStorage 사용)
     * window.location.href로 페이지 이동 시 사용
     */
    flash(type, message) {
        sessionStorage.setItem(TOAST_KEY, JSON.stringify({ type, message }));
    }
};

window.toast = toast;

// 페이지 로드 시 pending toast 확인
showPendingToast();

// htmx로 페이지 교체 후에도 pending toast 확인
document.addEventListener('htmx:afterSettle', showPendingToast);
