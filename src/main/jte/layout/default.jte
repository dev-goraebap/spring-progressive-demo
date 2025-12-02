@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content
@param gg.jte.Content subContent = @``

<!DOCTYPE html>
<html lang="ko" data-theme="dark" class="overflow-y-scroll">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="htmx-config" content='{"globalViewTransitions": true}'>
    <title>데브고래밥 | ${title}</title>

    <!-- 폰트 -->
    <link rel="stylesheet" href="/css/font.css">

    @if(isDev())
    <!-- 개발 환경: Vite dev server -->
    <link rel="stylesheet" href="http://localhost:5173/src/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    <script type="module" src="http://localhost:5173/src/app.js"></script>
    @else
    <!-- 프로덕션: manifest 기반 빌드 파일 -->
    <link rel="stylesheet" href="/${viteCss()}">
    <script type="module" src="/${viteJs()}"></script>
    @endif
</head>
<body hx-boost="true">
<div id="htmx-progress"></div>
<div class="min-h-screen flex justify-center">
    <!-- DESKTOP NAV UI -->
    <div class="hidden sm:block min-h-full w-auto">
        <div class="sticky top-0">
            <nav class="p-4">
                <ul class="flex flex-col">
                    <li class="tooltip tooltip-left" data-tip="About">
                        <a href="/about"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/about") ? "text-info" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.identification()</div>
                        </a>
                    </li>
                    <li class="p-2">
                        <div class="border-t border-base-300"></div>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="피드">
                        <a href="/"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.home()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="시리즈">
                        <a href="/series"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/series") ? "text-accent" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.book()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="RSS 큐레이션">
                        <a href="/curations"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/curations") ? "text-warning" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.news()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-left" data-tip="패치노트">
                        <a href="/patch-notes"
                           class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/patch-notes") ? "text-secondary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.info()</div>
                        </a>
                    </li>
                </ul>
            </nav>
            <footer></footer>
        </div>
    </div>

    <!-- MAIN CONTENT UI -->
    <div class="w-full sm:min-w-[598px] sm:w-[598px] sm:border-x border-base-300 pb-16 sm:pb-0">
        <main>
            ${content}
        </main>
    </div>

    <!-- DESKTOP SUB CONTENT UI -->
    <div class="hidden lg:flex lg:flex-col w-[300px] p-4">

        <!-- 블로그 제목 -->
        <h2 class="text-xl font-bold mb-6">데브고래밥 블로그</h2>

        <!-- Sticky 컨텐츠 영역 -->
        <div class="sticky top-4">
            <!-- 패치노트 -->
            <div class="mb-6">
                <div class="bg-base-200 border border-base-300 rounded-xl p-4">
                    <h3 class="font-bold text-sm mb-2">패치노트</h3>
                    <p class="text-xs text-base-content/70">최근 업데이트 내용이 여기에 표시됩니다.</p>
                </div>
            </div>

            <!-- Music Player UI -->
            <div x-data>
                <div class="relative w-full pt-[70px]">
                    <%-- 바이닐 디스크 --%>
                    <div class="absolute top-0 left-1/2 -translate-x-1/2 w-[160px] h-[160px] z-10">
                        <div class="w-full h-full rounded-full overflow-hidden shadow-xl animate-[spin_4s_linear_infinite]"
                             :style="$store.bgm.isPlaying ? 'animation-play-state: running' : 'animation-play-state: paused'">
                            <img :src="$store.bgm.track?.cover || '/bgm/jane-doe.webp'" alt="Album Art"
                                 class="w-full h-full object-cover"/>
                            <%-- 중앙 원 --%>
                            <div class="absolute inset-0 flex justify-center items-center">
                                <div class="rounded-full w-10 h-10 bg-base-300 border-4 border-base-100"></div>
                            </div>
                        </div>
                    </div>

                    <%-- 컨트롤 패널 --%>
                    <div class="relative bg-base-200 border border-base-300 rounded-xl pt-24 pb-4 px-4">
                        <%-- 곡 제목 --%>
                        <p class="text-center text-sm font-medium mb-4 truncate"
                           x-text="$store.bgm.track?.title || 'No Track'"></p>

                        <%-- 프로그레스 바 --%>
                        <div class="flex items-center gap-2 mb-4">
                            <span class="text-xs text-base-content/70" x-text="$store.bgm.currentTime">0:00</span>
                            <input type="range" min="0" max="100"
                                   :value="$store.bgm.progress"
                                   @input="$store.bgm.seek($event.target.value)"
                                   class="range range-xs range-primary flex-1"/>
                            <span class="text-xs text-base-content/70" x-text="$store.bgm.duration">0:00</span>
                        </div>

                        <%-- 컨트롤 버튼 --%>
                        <div class="flex justify-center items-center gap-4">
                            <button @click="$store.bgm.prev()" class="btn btn-ghost btn-sm btn-circle">
                                <div class="w-5">@template.fragments.svg.prev()</div>
                            </button>
                            <button @click="$store.bgm.toggle()" class="btn btn-primary btn-circle">
                                <div class="w-5">
                                    <template x-if="!$store.bgm.isPlaying">@template.fragments.svg.play()</template>
                                    <template x-if="$store.bgm.isPlaying">@template.fragments.svg.pause()</template>
                                </div>
                            </button>
                            <button @click="$store.bgm.next()" class="btn btn-ghost btn-sm btn-circle">
                                <div class="w-5">@template.fragments.svg.next()</div>
                            </button>
                        </div>

                        <%-- 볼륨 --%>
                        <div class="flex items-center gap-2 mt-4">
                            <div class="w-4">@template.fragments.svg.volume()</div>
                            <input type="range" min="0" max="100"
                                   :value="$store.bgm.volume"
                                   @input="$store.bgm.setVolume($event.target.value)"
                                   class="range range-xs flex-1"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- MOBILE BOTTOM NAV -->
    <nav class="sm:hidden fixed bottom-0 left-0 right-0 bg-base-100 border-t border-base-300 z-[9999]">
        <ul class="flex items-end h-16 pb-2">
            <li class="flex-1">
                <a href="/about"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/about") ? "text-info" : "text-base-content/70"}">
                    <div class="w-5">@template.fragments.svg.identification()</div>
                    <span class="text-xs">About</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/curations"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/curations") ? "text-warning" : "text-base-content/70"}">
                    <div class="w-5">@template.fragments.svg.news()</div>
                    <span class="text-xs">큐레이션</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/") ? "text-primary" : "text-base-content/70"}">
                    <div class="w-5">@template.fragments.svg.home()</div>
                    <span class="text-xs">피드</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/series"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/series") ? "text-accent" : "text-base-content/70"}">
                    <div class="w-5">@template.fragments.svg.book()</div>
                    <span class="text-xs">시리즈</span>
                </a>
            </li>
            <li class="flex-1">
                <a href="/patch-notes"
                   class="flex flex-col items-center gap-1 p-2 ${isCurrentPath("/patch-notes") ? "text-secondary" : "text-base-content/70"}">
                    <div class="w-5">@template.fragments.svg.info()</div>
                    <span class="text-xs">패치노트</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
</body>
</html>
