import './style.css';
import htmx from 'htmx.org';
import Alpine from 'alpinejs';

// HTMX를 전역으로
window.htmx = htmx;

// Alpine 시작
window.Alpine = Alpine;
Alpine.start();
