package xyz.goraebap.blog.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.blog.infra.view_model.FcmSubscriptionViewModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface FcmSubscriptionViewMapper {

    List<FcmSubscriptionViewModel> findAll(
            @Param("search") String search,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countAll(@Param("search") String search);

    List<Map<String, Object>> countByBrowser();

    List<Map<String, Object>> countByOs();

    List<Map<String, Object>> countByDeviceType();
}
