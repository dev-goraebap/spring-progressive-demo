import './style.css';
import htmx from 'htmx.org';
import Alpine from 'alpinejs';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 전역 등록
window.Alpine = Alpine;

// jte 하위 모든 .js 파일 자동 로드 (bgm.js 제외)
import.meta.glob([
    '../shared/**/*.js',
    '../pages/**/*.js',
    '../layout/**/*.js',
    '../entities/**/*.js',
    '!../shared/components/bgm.js',
], {eager: true});

// BGM: admin 페이지 제외하고 동적 로드
if (!window.location.pathname.startsWith('/admin')) {
    import('../shared/components/bgm.js');
}

// Alpine 시작
Alpine.start();
