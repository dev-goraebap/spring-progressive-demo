package xyz.goraebap.blog.app.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.FcmSubscriptionEntity;
import xyz.goraebap.blog.app.admin.domain.FcmSubscriptionRepository;
import xyz.goraebap.blog.contract.fcm.FcmSubscriber;
import xyz.goraebap.blog.contract.fcm.SubscribeFcmDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService implements FcmSubscriber {

    private final FcmSubscriptionRepository fcmSubscriptionRepository;

    @Override
    @Transactional
    public void subscribe(SubscribeFcmDto dto) {
        String token = dto.getToken();

        // 이미 등록된 토큰이면 무시
        if (fcmSubscriptionRepository.existsByToken(token)) {
            log.debug("[FCM] 이미 등록된 토큰: {}...", token.substring(0, 20));
            return;
        }

        // 저장
        FcmSubscriptionEntity entity = FcmSubscriptionEntity.create(
                token,
                dto.getIpAddress(),
                dto.getBrowser(),
                dto.getOs(),
                dto.getDeviceType()
        );
        fcmSubscriptionRepository.save(entity);

        log.info("[FCM] 새 구독 등록 - IP: {}, Browser: {}, OS: {}",
                dto.getIpAddress(), dto.getBrowser(), dto.getOs());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubscribed(String token) {
        return fcmSubscriptionRepository.existsByToken(token);
    }

    @Override
    @Transactional
    public void deleteInvalidTokens(List<String> tokens) {
        if (tokens.isEmpty()) return;

        tokens.forEach(fcmSubscriptionRepository::deleteByToken);
        log.info("[FCM] 무효 토큰 {}개 삭제 완료", tokens.size());
    }
}
