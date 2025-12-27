@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content

!{var siteName = "데브고래밥 스튜디오";}

<!DOCTYPE html>
<html lang="ko" data-theme="${getTheme()}" class="overflow-y-scroll">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="htmx-config" content='{"globalViewTransitions": true}'>

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
    <script type="module" src="/${viteJs("admin")}"></script>
    @endif
</head>
<body hx-boost="true">
<div id="htmx-progress"></div>
@template.shared.layout.common.pwaInstallBanner()

<div class="drawer lg:drawer-open">
    <input id="admin-drawer" type="checkbox" class="drawer-toggle"/>

    <%-- 메인 컨텐츠 영역 --%>
    <div class="drawer-content flex flex-col">
        <%-- 모바일 헤더 --%>
        <header class="lg:hidden sticky top-0 z-10 bg-base-100 border-b border-base-300">
            <div class="navbar">
                <div class="flex-none">
                    <label for="admin-drawer" class="btn btn-square btn-ghost">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="inline-block h-6 w-6 stroke-current">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
                        </svg>
                    </label>
                </div>
                <div class="flex-1">
                    <a href="/admin/posts" class="btn btn-ghost text-lg font-bold">${siteName}</a>
                </div>
                <div class="flex-none">
                </div>
            </div>
        </header>

        <%-- 컨텐츠 --%>
        <main class="flex-1 flex flex-col">
            ${content}
        </main>
    </div>

    <%-- 사이드바 --%>
    <div class="drawer-side z-20">
        <label for="admin-drawer" aria-label="close sidebar" class="drawer-overlay"></label>
        <div class="border-r border-base-300 h-full bg-base-100">
            @template.shared.layout.admin.sidebar()
        </div>
    </div>
</div>

@template.shared.components.modal()
</body>
</html>
