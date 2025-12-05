package xyz.goraebap.spring_progressive_demo.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel;

import java.util.List;

@Mapper
public interface SeriesViewMapper {
    List<SeriesViewModel> findAllSeries();
}
