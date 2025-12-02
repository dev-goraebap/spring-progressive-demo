import './style.css';
import htmx from 'htmx.org';
import Alpine from 'alpinejs';

import { registerBgmStore } from './stores/bgm.js';
import { setupHtmxProgress } from './htmx-progress.js';

// HTMX 전역 등록
window.htmx = htmx;

// Alpine 스토어 등록
registerBgmStore(Alpine);

// Alpine 시작
window.Alpine = Alpine;
Alpine.start();

// HTMX 프로그레스 바 설정
setupHtmxProgress();
