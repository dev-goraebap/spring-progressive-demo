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
messaging.onBackgroundMessage((payload) => {
    console.log('[FCM SW] 백그라운드 메시지 수신:', payload);

    const { title, body, icon } = payload.notification || {};
    const { url } = payload.data || {};

    self.registration.showNotification(title || '새 알림', {
        body: body || '',
        icon: icon || '/images/logo.png',
        badge: '/images/logo.png',
        data: { url: url || '/' }
    });
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
