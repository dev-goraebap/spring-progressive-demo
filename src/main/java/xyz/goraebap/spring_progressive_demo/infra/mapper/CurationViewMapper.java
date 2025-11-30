package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.CurationViewModel;

import java.util.List;

@Mapper
public interface CurationViewMapper {
    List<CurationViewModel> findItems(
        @Param("sortBy") String sortBy,
        @Param("sortDir") String sortDir,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    int countItems();
}
