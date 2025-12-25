package xyz.goraebap.blog.app.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCuratedSourceFormRequest {

    @NotBlank(message = "소스 이름은 필수입니다.")
    @Size(max = 100, message = "소스 이름은 100자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "RSS URL은 필수입니다.")
    private String url;

    private String isActiveYn = "Y";
}
