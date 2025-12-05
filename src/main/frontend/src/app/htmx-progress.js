/**
 * HTMX 프로그레스 바 이벤트 리스너 등록
 */
document.addEventListener('htmx:beforeRequest', () => {
    document.body.classList.add('htmx-request');
    document.body.classList.remove('htmx-request-complete');
});

document.addEventListener('htmx:afterRequest', () => {
    document.body.classList.remove('htmx-request');
    document.body.classList.add('htmx-request-complete');
    setTimeout(() => {
        document.body.classList.remove('htmx-request-complete');
        const progress = document.getElementById('htmx-progress');
        if (progress) progress.style.width = '0';
    }, 200);
});
