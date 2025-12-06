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
    <link rel="icon" type="image/png" href="/images/logo.png">

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
<body hx-boost="true" x-data="{ drawer: false }">
<div id="htmx-progress"></div>

<!-- MOBILE DRAWER OVERLAY -->
<div x-show="drawer" x-cloak
     @click="drawer = false"
     class="sm:hidden fixed inset-0 bg-black/50 z-[10000]"
     x-transition:enter="transition ease-out duration-300"
     x-transition:enter-start="opacity-0"
     x-transition:enter-end="opacity-100"
     x-transition:leave="transition ease-in duration-200"
     x-transition:leave-start="opacity-100"
     x-transition:leave-end="opacity-0">
</div>

<!-- MOBILE DRAWER -->
<div x-show="drawer" x-cloak
     class="sm:hidden fixed top-0 right-0 h-full w-[300px] bg-base-100 z-[10001] overflow-y-auto p-4 pb-20"
     x-transition:enter="transition ease-out duration-300"
     x-transition:enter-start="translate-x-full"
     x-transition:enter-end="translate-x-0"
     x-transition:leave="transition ease-in duration-200"
     x-transition:leave-start="translate-x-0"
     x-transition:leave-end="translate-x-full">

    <!-- 상단: 테마 토글 + 닫기 버튼 -->
    <div class="flex justify-between items-center mb-4">
        @template.shared.components.themeToggle()
        <button @click="drawer = false" class="btn btn-ghost btn-sm btn-circle">
            <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12"/>
            </svg>
        </button>
    </div>

    @template.shared.components.aside()
</div>

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
        <div class="sticky top-4">
            @template.shared.components.aside()
        </div>
    </div>

    <!-- MOBILE BOTTOM NAV -->
    <nav class="sm:hidden fixed bottom-0 left-0 right-0 bg-base-100 border-t border-base-300 z-[9999]">
        <ul class="flex items-center h-16">
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
                <a href="/curations"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/curations") ? "text-warning" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.news()</div>
                    <span class="text-xs">큐레이션</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/patch-notes"
                   class="flex flex-col items-center gap-1 p-2 ${isPathStartsWith("/patch-notes") ? "text-secondary" : "text-base-content/70"}">
                    <div class="w-5">@template.shared.svg.info()</div>
                    <span class="text-xs">패치노트</span>
                </a>
            </li>
            <li class="flex-1">
                <button @click="drawer = true"
                        class="flex flex-col items-center gap-1 p-2 text-base-content/70 w-full">
                    <%-- BGM 재생 중: 앨범아트 + 레인보우 테두리 --%>
                    <div x-show="$store.bgm.isPlaying" x-cloak
                         class="relative w-8 h-8">
                        <div class="absolute inset-0 rounded-full bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-500 animate-spin-slow opacity-70"></div>
                        <div class="absolute inset-[2px] rounded-full overflow-hidden">
                            <img :src="$store.bgm.track?.cover || '/bgm/jane-doe.webp'"
                                 alt="Now Playing"
                                 class="w-full h-full object-cover"/>
                        </div>
                    </div>
                    <%-- BGM 정지: 메뉴 아이콘 --%>
                    <div x-show="!$store.bgm.isPlaying" class="w-5">
                        @template.shared.svg.menu()
                    </div>
                    <span class="text-xs" x-text="$store.bgm.isPlaying ? '' : '더보기'">더보기</span>
                </button>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
