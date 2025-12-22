package xyz.goraebap.spring_progressive_demo.app.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.TagEntity;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.TagRepository;
import xyz.goraebap.spring_progressive_demo.shared.exception.BadRequestException;
import xyz.goraebap.spring_progressive_demo.shared.exception.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public TagEntity create(String name, Long userId) {
        if (tagRepository.existsByName(name)) {
            throw new BadRequestException("이미 존재하는 태그입니다.");
        }
        var tag = TagEntity.create(name, userId);
        return tagRepository.save(tag);
    }

    public TagEntity update(Long id, String name) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("태그를 찾을 수 없습니다."));

        if (!tag.getName().equals(name) && tagRepository.existsByName(name)) {
            throw new BadRequestException("이미 존재하는 태그입니다.");
        }

        tag.update(name);
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("태그를 찾을 수 없습니다."));
        tagRepository.delete(tag);
    }
}
