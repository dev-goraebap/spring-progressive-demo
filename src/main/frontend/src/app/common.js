import htmx from 'htmx.org';
import Alpine from 'alpinejs';
import './htmxGlobalEvents.js';
import { createFilePond, destroyFilePond } from '../shared/utils/filepond.js';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 전역 등록
window.Alpine = Alpine;

// FilePond 전역 등록 (모달에서 사용)
window.createFilePond = createFilePond;
window.destroyFilePond = destroyFilePond;

// 공통 JS 로드 (*.app.js, *.admin.js 제외)
import.meta.glob([
    '../**/*.js',
    '!../app/**',
    '!../**/*.app.js',
    '!../**/*.admin.js',
], {eager: true});
