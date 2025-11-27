import './style.css';
import htmx from 'htmx.org';
import Alpine from 'alpinejs';

// HTMX를 전역으로
window.htmx = htmx;

// Alpine 시작
window.Alpine = Alpine;
Alpine.start();

// HTMX 프로그레스 바
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
