package xyz.goraebap.blog.contract.fcm;

import java.util.List;

public interface FcmSubscriber {
    void subscribe(SubscribeFcmDto dto);
    boolean isSubscribed(String token);
    void deleteInvalidTokens(List<String> tokens);
}
