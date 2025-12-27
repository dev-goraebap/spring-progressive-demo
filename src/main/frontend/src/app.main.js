import './app.common.js';

// main 전용 JS 로드
import.meta.glob([
    '../**/*.main.js',
], {eager: true});

// Alpine 시작
Alpine.start();
