package xyz.goraebap.blog.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.blog.infra.mapper.FcmSubscriptionViewMapper;
import xyz.goraebap.blog.infra.view_model.FcmSubscriptionViewModel;
import xyz.goraebap.blog.infra.view_model.Pagination;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FcmSubscriptionQueryService {

    private final FcmSubscriptionViewMapper fcmSubscriptionViewMapper;

    public Pagination<FcmSubscriptionViewModel> getSubscriptionsWithPagination(
            String search,
            int page,
            int size
    ) {
        int offset = (page - 1) * size;
        var items = fcmSubscriptionViewMapper.findAll(search, size, offset);
        int totalCount = fcmSubscriptionViewMapper.countAll(search);
        return Pagination.of(items, page, totalCount, "desc", size);
    }

    public int getTotalCount() {
        return fcmSubscriptionViewMapper.countAll(null);
    }

    public List<Map<String, Object>> getStatsByBrowser() {
        return fcmSubscriptionViewMapper.countByBrowser();
    }

    public List<Map<String, Object>> getStatsByOs() {
        return fcmSubscriptionViewMapper.countByOs();
    }

    public List<Map<String, Object>> getStatsByDeviceType() {
        return fcmSubscriptionViewMapper.countByDeviceType();
    }
}
