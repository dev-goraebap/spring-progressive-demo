@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content

!{var siteName = "데브고래밥 스튜디오";}

<!DOCTYPE html>
<html lang="ko" data-theme="${getTheme()}" class="overflow-y-scroll">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>${siteName} | ${title}</title>
    <link rel="icon" type="image/png" href="/images/logo.png">

    <!-- PWA -->
    <link rel="manifest" href="/manifest.json">
    <meta name="theme-color" content="#334155">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="apple-touch-icon" href="/images/logo.png">

    <!-- Service Worker 등록 -->
    <script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/sw.js');
    }
    </script>

    <%-- 쿠키 없을 때 시스템 설정 감지 (FOUC 방지) --%>
    @if(getTheme() == null)
        <script>
            document.documentElement.setAttribute('data-theme',
                window.matchMedia('(prefers-color-scheme: light)').matches ? 'cupcake' : 'dark');
        </script>
    @endif

    <!-- 폰트 -->
    <link rel="stylesheet" href="/css/font.css">

    @if(isDev())
    <!-- 개발 환경: Vite dev server -->
    <link rel="stylesheet" href="http://localhost:5173/src/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    <script type="module" src="http://localhost:5173/src/app.admin.js"></script>
    @else
    <!-- 프로덕션: manifest 기반 빌드 파일 -->
    <link rel="stylesheet" href="/${viteCss()}">
    <script type="module" src="/${viteJs("app.admin")}"></script>
    @endif
</head>
<body>
    @template.shared.layout.common.pwaInstallBanner()
    ${content}
</body>
</html>
