package xyz.goraebap.spring_progressive_demo.infra.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.goraebap.spring_progressive_demo.infra.mapper.SeriesViewMapper;
import xyz.goraebap.spring_progressive_demo.infra.view_model.SeriesViewModel;
import xyz.goraebap.spring_progressive_demo.shared.config.R2Properties;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeriesQueryService {

    private final R2Properties r2Properties;
    private final ObjectMapper objectMapper;
    private final SeriesViewMapper seriesViewMapper;

    public List<SeriesViewModel> getAllSeries() {
        var seriesList = seriesViewMapper.findAllSeries();
        seriesList.forEach(this::enrichThumbnail);
        return seriesList;
    }

    private void enrichThumbnail(SeriesViewModel series) {
        if (series.getThumbnailKey() != null) {
            series.setThumbnailUrl(r2Properties.getPublicUrl(series.getThumbnailKey()));
        }

        if (series.getThumbnailMetadata() != null) {
            try {
                JsonNode metadata = objectMapper.readTree(series.getThumbnailMetadata());
                if (metadata.has("dominantColor")) {
                    series.setThumbnailDominantColor(metadata.get("dominantColor").asText());
                }
            } catch (Exception e) {
                log.warn("Failed to parse thumbnail metadata: {}", e.getMessage());
            }
        }
    }
}
