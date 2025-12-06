package xyz.goraebap.spring_progressive_demo.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.SeriesViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesDetailViewModel;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesQueryService {

    private final SeriesViewMapper seriesViewMapper;
    private final ThumbnailEnricher thumbnailEnricher;

    public List<SeriesViewModel> getAllSeries() {
        var seriesList = seriesViewMapper.findAllSeries();
        seriesList.forEach(thumbnailEnricher::enrich);
        return seriesList;
    }

    public SeriesDetailViewModel getSeriesWithPosts(String slug) {
        var series = seriesViewMapper.findSeriesWithPosts(slug);
        if (series == null) {
            return null;
        }

        thumbnailEnricher.enrich(series);
        series.getPosts().forEach(thumbnailEnricher::enrich);

        return series;
    }
}
