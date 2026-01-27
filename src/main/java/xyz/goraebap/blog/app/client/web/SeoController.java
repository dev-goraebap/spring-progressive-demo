package xyz.goraebap.blog.app.client.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.goraebap.blog.infra.service.SitemapQueryService;
import xyz.goraebap.blog.infra.view_model.SitemapUrlViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * SEO 관련 엔드포인트 컨트롤러
 * - sitemap.xml: 검색 엔진용 URL 목록
 * - robots.txt: 크롤러 접근 제어
 */
@RestController
@RequiredArgsConstructor
public class SeoController {

    private final SitemapQueryService sitemapQueryService;

    @Value("${app.base-url:https://dev.goraebap.xyz}")
    private String baseUrl;

    /**
     * sitemap.xml 생성
     * - 발행된 게시물, 시리즈 포함
     * - 캐싱 적용 (12시간)
     */
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {
        List<SitemapUrlViewModel> urls = sitemapQueryService.getAllUrls();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (SitemapUrlViewModel url : urls) {
            xml.append("  <url>\n");
            xml.append("    <loc>").append(baseUrl).append(url.getLoc()).append("</loc>\n");

            if (url.getLastmod() != null) {
                xml.append("    <lastmod>").append(url.getLastmod().format(formatter)).append("</lastmod>\n");
            }

            if (url.getChangefreq() != null) {
                xml.append("    <changefreq>").append(url.getChangefreq()).append("</changefreq>\n");
            }

            if (url.getPriority() != null) {
                xml.append("    <priority>").append(url.getPriority()).append("</priority>\n");
            }

            xml.append("  </url>\n");
        }

        xml.append("</urlset>");

        return ResponseEntity.ok(xml.toString());
    }

    /**
     * robots.txt 생성
     * - 관리자 페이지, 게스트 페이지 크롤링 차단
     * - sitemap 위치 명시
     */
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> robots() {
        StringBuilder txt = new StringBuilder();
        txt.append("User-agent: *\n");
        txt.append("Allow: /\n");
        txt.append("Disallow: /admin/\n");
        txt.append("Disallow: /guest/\n");
        txt.append("Disallow: /api/\n");
        txt.append("\n");
        txt.append("Sitemap: ").append(baseUrl).append("/sitemap.xml\n");

        return ResponseEntity.ok(txt.toString());
    }
}
