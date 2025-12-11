import htmx from 'htmx.org';
import Alpine from 'alpinejs';
import './htmx-global-events.js';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 전역 등록
window.Alpine = Alpine;

// 공통 JS 로드 (*.app.js, *.admin.js 제외)
import.meta.glob([
    '../**/*.js',
    '!../app/**',
    '!../**/*.app.js',
    '!../**/*.admin.js',
], {eager: true});
