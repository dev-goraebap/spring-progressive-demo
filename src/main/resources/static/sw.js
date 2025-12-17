// 최소한의 Service Worker - PWA 설치 조건 충족용
// 캐시 없음, 네트워크 요청 가로채지 않음

self.addEventListener('install', () => {
    self.skipWaiting();
});

self.addEventListener('activate', () => {
    self.clients.claim();
});
