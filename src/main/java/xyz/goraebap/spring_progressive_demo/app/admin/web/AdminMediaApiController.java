package xyz.goraebap.spring_progressive_demo.app.admin.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.goraebap.spring_progressive_demo.app.admin.app.MediaService;

@RestController
@RequestMapping("/api/v1/admin/media")
@RequiredArgsConstructor
public class AdminMediaApiController {

    private final MediaService mediaService;

    @PostMapping
    public ResponseEntity<MediaService.MediaUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        // TODO: 실제 인증된 사용자 ID로 교체
        Long userId = 1L;
        var response = mediaService.uploadFile(file, userId);
        return ResponseEntity.ok(response);
    }
}
