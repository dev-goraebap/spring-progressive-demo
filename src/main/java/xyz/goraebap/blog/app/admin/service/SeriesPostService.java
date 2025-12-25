package xyz.goraebap.blog.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.blog.app.admin.domain.*;
import xyz.goraebap.blog.shared.exception.BadRequestException;
import xyz.goraebap.blog.shared.exception.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeriesPostService {

    private final SeriesRepository seriesRepository;
    private final PostRepository postRepository;
    private final SeriesPostRepository seriesPostRepository;

    public void addPost(Long seriesId, Long postId) {
        // 시리즈 존재 확인
        if (!seriesRepository.existsById(seriesId)) {
            throw new NotFoundException("시리즈를 찾을 수 없습니다.");
        }

        // 포스트 존재 확인
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("포스트를 찾을 수 없습니다.");
        }

        // 중복 확인
        if (seriesPostRepository.findBySeriesIdAndPostId(seriesId, postId).isPresent()) {
            throw new BadRequestException("이미 시리즈에 포함된 포스트입니다.");
        }

        var seriesPost = SeriesPostEntity.create(seriesId, postId);
        seriesPostRepository.save(seriesPost);
    }

    public void removePost(Long seriesId, Long postId) {
        seriesPostRepository.deleteBySeriesIdAndPostId(seriesId, postId);
    }

    public void updateOrders(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return;

        var ids = items.stream().map(OrderItem::id).toList();
        var entities = seriesPostRepository.findByIdIn(ids);

        for (var entity : entities) {
            var item = items.stream()
                    .filter(i -> i.id().equals(entity.getId()))
                    .findFirst();
            item.ifPresent(i -> entity.updateSortOrder(i.sortOrder()));
        }

        seriesPostRepository.saveAll(entities);
    }

    public record OrderItem(Long id, int sortOrder) {}
}
