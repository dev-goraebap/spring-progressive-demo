package xyz.goraebap.spring_progressive_demo.app.post.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.goraebap.spring_progressive_demo.app.post.PostService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ViewCountHelper {

    private static final String VIEWED_POSTS_COOKIE = "viewed_posts";
    private static final int COOKIE_MAX_AGE = 3600; // 1시간

    private final PostService postService;

    public void process(Long postId, String viewedPosts, HttpServletResponse response) {
        Set<String> viewedSet = viewedPosts.isEmpty()
                ? new HashSet<>()
                : Arrays.stream(viewedPosts.split(",")).collect(Collectors.toSet());

        String postIdStr = String.valueOf(postId);
        if (!viewedSet.contains(postIdStr)) {
            postService.incrementViewCount(postId);
            viewedSet.add(postIdStr);

            Cookie cookie = new Cookie(VIEWED_POSTS_COOKIE, String.join(",", viewedSet));
            cookie.setMaxAge(COOKIE_MAX_AGE);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
    }

    public String getCookieName() {
        return VIEWED_POSTS_COOKIE;
    }
}
