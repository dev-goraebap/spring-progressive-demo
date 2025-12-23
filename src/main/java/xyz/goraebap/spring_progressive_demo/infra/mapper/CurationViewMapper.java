package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminCuratedItemViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminCuratedSourceViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface CurationViewMapper {

    // 클라이언트: 큐레이션 항목 목록
    List<CurationViewModel> findItems(
        @Param("sortBy") String sortBy,
        @Param("sortDir") String sortDir,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countItems();

    // 관리자: 소스 목록
    List<AdminCuratedSourceViewModel> findAllSources();

    AdminCuratedSourceViewModel findSourceById(@Param("id") Long id);

    // 관리자: 항목 목록
    List<AdminCuratedItemViewModel> findAdminItems(
        @Param("sourceId") Long sourceId,
        @Param("sortBy") String sortBy,
        @Param("sortDir") String sortDir,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countAdminItems(@Param("sourceId") Long sourceId);

    // 통계
    Map<String, Object> getStats();
}
