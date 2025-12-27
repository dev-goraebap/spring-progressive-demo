import './app.common.js';
import './app/states/bgm.store';

// main 전용 JS 로드
import.meta.glob([
    '../views/**/*.main.js',
], {eager: true});

// Alpine 시작
Alpine.start();
