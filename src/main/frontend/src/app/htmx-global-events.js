import Alpine from "alpinejs";

/**
 * 프로그레스바 UI
 * 페이지 전환시 오래 걸리는 작업에 대해서 상단 프로그래스바 UI 노출
 */
window.addEventListener('htmx:beforeRequest', () => {
    document.documentElement.classList.remove('htmx-request-complete');
    document.documentElement.classList.add('htmx-request');
});

window.addEventListener('htmx:afterRequest', () => {
    document.documentElement.classList.remove('htmx-request');
    document.documentElement.classList.add('htmx-request-complete');
    setTimeout(() => {
        document.documentElement.classList.remove('htmx-request-complete');
    }, 300);
});

/**
 * 전역 에러 핸들러
 */
document.addEventListener('htmx:responseError', (e) => {
    console.debug(e);

    if (e.detail.target.id === 'modal-content') {
        Alpine.store('modal').onClose();
    }

    Alpine.store('toast').show(e?.detail?.error);
});