import './app.common.js';
import './app/states/bgm.store';
import './app/states/notification.store';

// Prism.js 코드 하이라이팅
import Prism from 'prismjs';
import 'prismjs/components/prism-markup';
import 'prismjs/components/prism-css';
import 'prismjs/components/prism-javascript';
import 'prismjs/components/prism-typescript';
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-kotlin';
import 'prismjs/components/prism-bash';
import 'prismjs/components/prism-sql';
import 'prismjs/components/prism-json';
import 'prismjs/components/prism-yaml';

// main 전용 JS 로드
import.meta.glob([
    '../views/**/*.main.js',
], {eager: true});

// 코드 하이라이팅 실행
function highlightCode() {
    Prism.highlightAll();
}
document.addEventListener('DOMContentLoaded', highlightCode);
document.addEventListener('htmx:afterSwap', highlightCode);

// Alpine 시작
Alpine.start();
