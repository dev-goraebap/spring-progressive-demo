// Firebase Messaging Service Worker
importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js');

// Firebase 설정 - src/shared/libs/firebase.js와 동일하게 유지할 것
firebase.initializeApp({
    apiKey: "AIzaSyBGq3hBKLEvMa8lmN4K7dLHXT79KYXLUbs",
    authDomain: "dev-goraebap-blog.firebaseapp.com",
    projectId: "dev-goraebap-blog",
    storageBucket: "dev-goraebap-blog.firebasestorage.app",
    messagingSenderId: "987444327002",
    appId: "1:987444327002:web:6a0ca56df53e950192de63"
});

const messaging = firebase.messaging();

// 백그라운드 메시지 수신
// NOTE: notification 페이로드가 있으면 FCM이 자동으로 알림을 표시함
// data-only 메시지일 때만 수동으로 표시 필요
messaging.onBackgroundMessage((payload) => {
    console.log('[FCM SW] 백그라운드 메시지 수신:', payload);

    // notification 페이로드가 있으면 FCM이 자동 표시하므로 스킵
    if (payload.notification) {
        return;
    }

    // data-only 메시지일 때만 수동 표시
    const { title, body, url } = payload.data || {};
    if (title) {
        self.registration.showNotification(title, {
            body: body || '',
            icon: '/images/logo.png',
            badge: '/images/logo.png',
            data: { url: url || '/' }
        });
    }
});

// 알림 클릭 시 해당 URL로 이동
self.addEventListener('notificationclick', (event) => {
    event.notification.close();

    const url = event.notification.data?.url || '/';

    event.waitUntil(
        clients.matchAll({ type: 'window', includeUncontrolled: true })
            .then((clientList) => {
                for (const client of clientList) {
                    if (client.url.includes(self.location.origin) && 'focus' in client) {
                        client.navigate(url);
                        return client.focus();
                    }
                }
                return clients.openWindow(url);
            })
    );
});
