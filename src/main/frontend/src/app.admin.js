import './app.common.js';

// admin 전용 JS 로드
import.meta.glob([
    '../**/*.admin.js',
], {eager: true});

// Alpine 시작
Alpine.start();
