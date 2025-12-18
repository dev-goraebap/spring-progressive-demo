package xyz.goraebap.spring_progressive_demo.app.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotNull
        Integer avatarNo,

        @NotBlank
        @Size(max = 50)
        String nickname,

        @NotBlank
        @Size(max = 1000)
        String comment
) {
}
