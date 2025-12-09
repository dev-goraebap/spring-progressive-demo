package xyz.goraebap.spring_progressive_demo.app.auth;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import lombok.extern.slf4j.Slf4j;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.goraebap.spring_progressive_demo.app.auth.domain.UserEntity;
import xyz.goraebap.spring_progressive_demo.app.auth.domain.UserRepository;
import xyz.goraebap.spring_progressive_demo.shared.security.JwtProvider;

import java.util.Base64;
import java.util.Optional;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String ISSUER = "데브고래밥 스튜디오";

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final DefaultCodeVerifier codeVerifier = createCodeVerifier();

    private static DefaultCodeVerifier createCodeVerifier() {
        DefaultCodeVerifier verifier = new DefaultCodeVerifier(
                new DefaultCodeGenerator(),
                new SystemTimeProvider()
        );
        // 시간 허용 범위 확대: ±2 윈도우 (약 1분)
        verifier.setAllowedTimePeriodDiscrepancy(2);
        return verifier;
    }

    /**
     * TOTP 코드 검증 후 JWT 발급
     */
    @Transactional(readOnly = true)
    public boolean login(String code, HttpServletResponse response) {
        log.info("=== TOTP 로그인 디버깅 ===");
        log.info("입력된 코드: [{}], 길이: {}", code, code.length());

        // ADMIN 유저 찾기
        Optional<UserEntity> adminOpt = userRepository.findByRole("ADMIN");
        log.info("Admin 찾음: {}", adminOpt.isPresent());
        if (adminOpt.isEmpty()) {
            return false;
        }

        UserEntity admin = adminOpt.get();
        log.info("TOTP Secret: {}", admin.getTotpSecret());
        if (admin.getTotpSecret() == null) {
            return false;
        }

        // TOTP 검증
        boolean isValid = codeVerifier.isValidCode(admin.getTotpSecret(), code);
        log.info("검증 결과: {}", isValid);
        if (!isValid) {
            return false;
        }

        // JWT 발급 및 쿠키 설정
        String accessToken = jwtProvider.createAccessToken(admin.getId(), admin.getRole());
        setAccessTokenCookie(response, accessToken);

        return true;
    }

    /**
     * 로그아웃 (쿠키 삭제)
     */
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * TOTP Secret 생성 및 QR 코드 반환
     */
    @Transactional
    public String setupTotp(Long userId) throws Exception {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Secret 생성
        String secret = secretGenerator.generate();
        user.setTotpSecret(secret);
        userRepository.save(user);

        // QR 코드 생성
        QrData qrData = new QrData.Builder()
                .label(user.getEmail())
                .secret(secret)
                .issuer(ISSUER)
                .build();

        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] qrImage = qrGenerator.generate(qrData);

        return getDataUriForImage(qrImage, qrGenerator.getImageMimeType());
    }

    /**
     * TOTP가 설정되어 있는지 확인
     */
    public boolean isTotpConfigured() {
        return userRepository.findByRole("ADMIN")
                .map(user -> user.getTotpSecret() != null)
                .orElse(false);
    }

    private void setAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7200); // 2시간
        // cookie.setSecure(true); // HTTPS에서만 활성화
        response.addCookie(cookie);
    }
}
