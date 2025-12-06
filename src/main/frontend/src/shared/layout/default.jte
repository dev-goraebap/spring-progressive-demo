@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content
@param gg.jte.Content subContent = @``

<!DOCTYPE html>
<html lang="ko" data-theme="${getTheme()}" class="overflow-y-scroll">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="htmx-config" content='{"globalViewTransitions": true}'>
    <title>데브고래밥 | ${title}</title>

    <%-- 쿠키 없을 때 시스템 설정 감지 (FOUC 방지) --%>
    @if(getTheme() == null)
        <script>
            document.documentElement.setAttribute('data-theme',
                window.matchMedia('(prefers-color-scheme: light)').matches ? 'cupcake' : 'dark');
            console.log('실행됨!!');
        </script>
    @endif

    <!-- 폰트 -->
    <link rel="stylesheet" href="/css/font.css">

    @if(isDev())
    <!-- 개발 환경: Vite dev server -->
    <link rel="stylesheet" href="http://localhost:5173/src/app/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    <script type="module" src="http://localhost:5173/src/app/app.js"></script>
    @else
    <!-- 프로덕션: manifest 기반 빌드 파일 -->
    <link rel="stylesheet" href="/${viteCss()}">
    <script type="module" src="/${viteJs()}"></script>
    @endif
</head>
<body hx-boost="true">
<div id="htmx-progress"></div>

<!-- MOBILE THEME TOGGLE -->
<%--<div class="sm:hidden fixed top-2 right-2 z-[999]">--%>
<%--    <button @click="$store.theme.toggle()" class="btn btn-ghost btn-circle btn-sm">--%>
<%--        <div class="w-4" x-show="$store.theme.current === 'dark'">@template.shared.svg.sun()</div>--%>
<%--        <div class="w-4" x-show="$store.theme.current === 'cupcake'" x-cloak>@template.shared.svg.moon()</div>--%>
<%--    </button>--%>
<%--</div>--%>

<div class="min-h-screen flex justify-center">
    <!-- DESKTOP NAV UI -->
    <div class="hidden sm:block min-h-full w-auto">
        <div class="sticky top-0">
            <nav class="p-4">
                <ul class="flex flex-col">
                    <li class="tooltip tooltip-left" data-tip="About">
                        <a href="/about"
                           class="flex gap-4 items-center text-lg p-4 ${isPathStartsWith("/about") ? "text-info" : "text-base-content"}">
                            <div class="w-6">@template.shared.svg.identification()</div>
                        </a>
                    </li>
                    <li class="p-2">
                        <div class="border-t border-base-300"></div>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="피드">
                        <a href="/"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/") || isPathStartsWith("/posts") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.shared.svg.home()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="시리즈">
                        <a href="/series"
                           class="flex gap-4 items-center text-lg p-4 ${isPathStartsWith("/series") ? "text-accent" : "text-base-content"}">
                            <div class="w-6">@template.shared.svg.book()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="RSS 큐레이션">
                        <a href="/curations"
                           class="flex gap-4 items-center text-lg p-4 ${isPathStartsWith("/curations") ? "text-warning" : "text-base-content"}">
                            <div class="w-6">@template.shared.svg.news()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="패치노트">
                        <a href="/patch-notes"
                           class="flex gap-4 items-center text-lg p-4 ${isPathStartsWith("/patch-notes") ? "text-secondary" : "text-base-content"}">
                            <div class="w-6">@template.shared.svg.info()</div>
                        </a>
                    </li>
                    <li class="p-2">
                        <div class="border-t border-base-300"></div>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="테마 변경">
                        @template.shared.components.themeToggle()
                    </li>
                </ul>
            </nav>
            <footer></footer>
        </div>
    </div>

    <!-- MAIN CONTENT UI -->
    <div class="w-full sm:border-x border-base-300 pb-16 sm:pb-0 sm:min-w-[698px] sm:w-[698px]">
        <main>
            ${content}
        </main>
    </div>

    <!-- DESKTOP SUB CONTENT UI -->
    <div class="hidden lg:flex lg:flex-col min-w-[300px] w-[300px] p-4">

        <!-- Sticky 컨텐츠 영역 -->
        <div class="sticky top-4">
            <!-- 블로그 제목 -->
            <div class="mb-6 flex items-center gap-2">
                <div class="w-16">
                    @template.shared.components.img(dominantColor = "", url = "/images/logo.png")
                </div>
                <h2 class="text-xl font-bold">데브고래밥 블로그</h2>
            </div>
            <!-- Music Player UI -->
            @template.shared.components.bgm()
        </div>
    </div>

    <!-- MOBILE BOTTOM NAV -->
    <nav class="sm:hidden fixed bottom-0 left-0 right-0 bg-base-100 border-t border-base-300 z-[9999]">
        <ul class="flex items-end h-16 pb-2">
            <li class="flex-1">
                <a href="/about"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/about") ? "text-info" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.identification()</div>
                    <span class="text-xs">About</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/curations"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/curations") ? "text-warning" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.news()</div>
                    <span class="text-xs">큐레이션</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/") || isPathStartsWith("/posts") ? "text-primary" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.home()</div>
                    <span class="text-xs">피드</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/series"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/series") ? "text-accent" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.book()</div>
                    <span class="text-xs">시리즈</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/patch-notes"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/patch-notes") ? "text-secondary" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.info()</div>
                    <span class="text-xs">패치노트</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
