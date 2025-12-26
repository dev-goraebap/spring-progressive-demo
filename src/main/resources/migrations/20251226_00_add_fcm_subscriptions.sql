-- =====================================================
-- FCM 구독자 테이블
-- Firebase Cloud Messaging 푸시 알림 구독 정보 저장
-- =====================================================

------------------------------------------------------MIGRATE-----------------------------------------------------------

CREATE TABLE fcm_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(500) NOT NULL UNIQUE,         -- FCM 디바이스 토큰
    ip_address VARCHAR(45),                     -- 구독자 IP (IPv6 대응)
    browser VARCHAR(50),                        -- 브라우저 (Chrome, Safari 등)
    os VARCHAR(50),                             -- 운영체제 (Windows, Android 등)
    device_type VARCHAR(20),                    -- 디바이스 타입 (Desktop, Mobile, Tablet)
    created_at TIMESTAMP NOT NULL DEFAULT NOW() -- 구독 일시
);

COMMENT ON TABLE fcm_subscriptions IS 'FCM 푸시 알림 구독자 정보';
COMMENT ON COLUMN fcm_subscriptions.token IS 'FCM 디바이스 토큰';
COMMENT ON COLUMN fcm_subscriptions.ip_address IS '구독자 IP 주소';
COMMENT ON COLUMN fcm_subscriptions.browser IS '브라우저 종류';
COMMENT ON COLUMN fcm_subscriptions.os IS '운영체제';
COMMENT ON COLUMN fcm_subscriptions.device_type IS '디바이스 타입 (Desktop/Mobile/Tablet)';
COMMENT ON COLUMN fcm_subscriptions.created_at IS '구독 일시';

COMMIT;

------------------------------------------------------ROLLBACK----------------------------------------------------------
-- DROP TABLE IF EXISTS fcm_subscriptions;
