import htmx from 'htmx.org';
import Alpine from 'alpinejs';

import './app/events/htmx';
import './app/toast';
import './app/states/pwa.store';
import './app/states/modal.store';
import './app/states/themeToggle.store';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 전역 등록
window.Alpine = Alpine;

// 공통 JS 로드 (*.main.js, *.admin.js, *.editor.js 제외)
import.meta.glob([
    '../views/**/*.js',
    '!../views/**/*.main.js',
    '!../views/**/*.admin.js',
    '!../views/**/*.editor.js',
], {eager: true});
