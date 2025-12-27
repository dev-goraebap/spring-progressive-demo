import './app.common.js';
import './app/states/filepond.data';

// admin 전용 JS 로드
import.meta.glob([
    '../views/**/*.admin.js',
], {eager: true});

// Alpine 시작
Alpine.start();
