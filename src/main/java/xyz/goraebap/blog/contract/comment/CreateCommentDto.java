package xyz.goraebap.blog.contract.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
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
