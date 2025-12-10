/**
 * HTMX 프로그레스 바 이벤트 리스너 등록
 * - htmx boost 모드에서 body가 교체되어도 동작하도록 html에 클래스 적용
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
