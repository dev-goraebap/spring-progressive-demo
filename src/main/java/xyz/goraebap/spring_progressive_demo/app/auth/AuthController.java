package xyz.goraebap.spring_progressive_demo.app.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/admin/login")
    public String loginPage(Model model) {
        boolean totpConfigured = authService.isTotpConfigured();
        model.addAttribute("totpConfigured", totpConfigured);
        return "pages/admin/login/index";
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/admin/login")
    public String login(@RequestParam String code,
                        HttpServletResponse response,
                        Model model) {
        boolean success = authService.login(code, response);

        if (success) {
            return "redirect:/admin/posts";
        }

        model.addAttribute("error", "인증 코드가 올바르지 않습니다.");
        model.addAttribute("totpConfigured", true);
        return "pages/admin/login/index";
    }

    /**
     * 로그아웃
     */
    @PostMapping("/admin/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/admin/login";
    }

    /**
     * TOTP 설정 페이지 (최초 설정용)
     */
    @GetMapping("/admin/setup-totp")
    public String setupTotpPage(@RequestParam Long userId, Model model) {
        try {
            String qrCodeDataUri = authService.setupTotp(userId);
            model.addAttribute("qrCode", qrCodeDataUri);
            return "pages/admin/login/setup-totp";
        } catch (Exception e) {
            model.addAttribute("error", "TOTP 설정에 실패했습니다: " + e.getMessage());
            return "pages/admin/login/index";
        }
    }
}
