package xyz.goraebap.blog.shared.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.app.admin.domain.FcmSubscriptionRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseService {

    private final FcmSubscriptionRepository fcmSubscriptionRepository;

    @Value("${firebase.credentials-path:}")
    private String credentialsPath;

    private boolean initialized = false;

    @PostConstruct
    public void init() {
        if (credentialsPath == null || credentialsPath.isEmpty()) {
            log.warn("Firebase credentials path가 설정되지 않았습니다. FCM 발송이 비활성화됩니다.");
            return;
        }

        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            initialized = true;
            log.info("Firebase Admin SDK 초기화 완료 (credentials: {})", credentialsPath);
        } catch (IOException e) {
            log.error("Firebase Admin SDK 초기화 실패", e);
        }
    }

    /**
     * 전체 구독자에게 알림 발송 (최대 500개씩 배치)
     */
    public int sendToAll(List<String> tokens, String title, String body, String url) {
        if (!initialized) {
            log.warn("Firebase가 초기화되지 않아 발송을 건너뜁니다.");
            return 0;
        }

        if (tokens.isEmpty()) {
            return 0;
        }

        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("url", url != null ? url : "/")
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

            log.info("[FCM] 멀티캐스트 발송 완료 - 성공: {}, 실패: {}",
                    response.getSuccessCount(), response.getFailureCount());

            // 실패한 토큰 처리 (무효 토큰 삭제)
            if (response.getFailureCount() > 0) {
                List<String> tokensToDelete = new ArrayList<>();
                List<SendResponse> responses = response.getResponses();

                for (int i = 0; i < responses.size(); i++) {
                    SendResponse sendResponse = responses.get(i);
                    if (!sendResponse.isSuccessful() && sendResponse.getException() != null) {
                        String failedToken = tokens.get(i);
                        MessagingErrorCode errorCode = sendResponse.getException().getMessagingErrorCode();

                        log.warn("[FCM] 발송 실패 - token: {}..., error: {}",
                                failedToken.substring(0, 20), errorCode);

                        // 무효한 토큰은 삭제 대상에 추가
                        if (errorCode == MessagingErrorCode.UNREGISTERED ||
                            errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                            tokensToDelete.add(failedToken);
                        }
                    }
                }

                // 무효 토큰 일괄 삭제
                if (!tokensToDelete.isEmpty()) {
                    tokensToDelete.forEach(fcmSubscriptionRepository::deleteByToken);
                    log.info("[FCM] 무효 토큰 {} 개 삭제 완료", tokensToDelete.size());
                }
            }

            return response.getSuccessCount();
        } catch (FirebaseMessagingException e) {
            log.error("[FCM] 멀티캐스트 발송 실패", e);
            return 0;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
