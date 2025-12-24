package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.goraebap.spring_progressive_demo.infra.view_model.BlockedIpViewModel;

import java.util.List;

@Mapper
public interface BlockedIpViewMapper {

    List<BlockedIpViewModel> findAll(
            @Param("search") String search,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countAll(@Param("search") String search);

    BlockedIpViewModel findById(@Param("id") Long id);
}
