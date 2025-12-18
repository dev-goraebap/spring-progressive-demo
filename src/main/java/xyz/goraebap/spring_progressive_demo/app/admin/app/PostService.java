package xyz.goraebap.spring_progressive_demo.app.admin.app;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.admin.domain.PostRepository;
import xyz.goraebap.spring_progressive_demo.contract.post.ViewCounter;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements ViewCounter {

    private final PostRepository postRepository;

    @Async
    public void incrementViewCount(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.incrementViewCount();
            postRepository.save(post);
        });
    }
}
