import './style.css';
import htmx from 'htmx.org';
import Alpine from 'alpinejs';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 전역 등록
window.Alpine = Alpine;

// jte 하위 모든 .js 파일 자동 로드
import.meta.glob([
    '../shared/**/*.js',
    '../pages/**/*.js',
    '../layout/**/*.js',
    './!(app).js'  // app 폴더 내 app.js 제외한 파일들
], { eager: true });

// Alpine 시작
Alpine.start();
