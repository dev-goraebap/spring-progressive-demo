import { initializeApp } from 'firebase/app';
import { getMessaging, getToken, onMessage } from 'firebase/messaging';

// Firebase 설정 - sw.js와 동일하게 유지할 것
const firebaseConfig = {
    apiKey: "AIzaSyBGq3hBKLEvMa8lmN4K7dLHXT79KYXLUbs",
    authDomain: "dev-goraebap-blog.firebaseapp.com",
    projectId: "dev-goraebap-blog",
    storageBucket: "dev-goraebap-blog.firebasestorage.app",
    messagingSenderId: "987444327002",
    appId: "1:987444327002:web:6a0ca56df53e950192de63",
    measurementId: "G-1S62W2S65Y"
};

const app = initializeApp(firebaseConfig);
const messaging = getMessaging(app);

// VAPID 키 (Firebase Console > 프로젝트 설정 > 클라우드 메시징 > 웹 푸시 인증서)
const VAPID_KEY = 'BDfnzNwxgDZRHNZLDhMS3JSCPZ8HBfCGZxLTqH-eB_6GroINq9Aq3hF_-Ku0rR0LRO91fEj8Z019kDvd_49TzKU';

/**
 * FCM 토큰 발급
 * @returns {Promise<string|null>} FCM 토큰 또는 null
 */
export async function getFcmToken() {
    try {
        const token = await getToken(messaging, { vapidKey: VAPID_KEY });
        if (token) {
            console.log('[FCM] 토큰 발급 성공:', token.substring(0, 20) + '...');
            return token;
        }
        console.warn('[FCM] 토큰 발급 실패: 권한 없음');
        return null;
    } catch (error) {
        console.error('[FCM] 토큰 발급 에러:', error);
        return null;
    }
}

/**
 * FCM 토큰을 서버에 등록
 * @param {string} token FCM 토큰
 */
export async function registerFcmToken(token) {
    try {
        const response = await fetch('/api/fcm/subscribe', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                token,
                browser: getBrowserName(),
                os: getOsName(),
                deviceType: getDeviceType()
            })
        });

        if (response.ok) {
            console.log('[FCM] 토큰 등록 성공');
            return true;
        }
        console.error('[FCM] 토큰 등록 실패:', response.status);
        return false;
    } catch (error) {
        console.error('[FCM] 토큰 등록 에러:', error);
        return false;
    }
}

/**
 * 포그라운드 메시지 수신 핸들러
 */
export function onForegroundMessage(callback) {
    onMessage(messaging, (payload) => {
        console.log('[FCM] 포그라운드 메시지 수신:', payload);
        callback(payload);
    });
}

// 유틸: 브라우저 이름
function getBrowserName() {
    const ua = navigator.userAgent;
    if (ua.includes('Chrome') && !ua.includes('Edg')) return 'Chrome';
    if (ua.includes('Safari') && !ua.includes('Chrome')) return 'Safari';
    if (ua.includes('Firefox')) return 'Firefox';
    if (ua.includes('Edg')) return 'Edge';
    return 'Unknown';
}

// 유틸: OS 이름
function getOsName() {
    const ua = navigator.userAgent;
    if (ua.includes('Windows')) return 'Windows';
    if (ua.includes('Mac')) return 'macOS';
    if (ua.includes('Linux')) return 'Linux';
    if (ua.includes('Android')) return 'Android';
    if (ua.includes('iOS') || ua.includes('iPhone') || ua.includes('iPad')) return 'iOS';
    return 'Unknown';
}

// 유틸: 디바이스 타입
function getDeviceType() {
    const ua = navigator.userAgent;
    if (/Mobi|Android|iPhone|iPad/i.test(ua)) return 'MOBILE';
    return 'DESKTOP';
}
