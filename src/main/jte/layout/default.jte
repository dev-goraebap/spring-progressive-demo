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
    <div class="hidden sm:block min-h-full w-auto">
        <div class="sticky top-0">
            <nav class="p-4">
                <ul class="flex flex-col">
                    <li class="tooltip tooltip-right" data-tip="About">
                        <a href="/about" class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/about") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.identification()</div>
                        </a>
                    </li>
                    <li class="p-2">
                        <div class="border-t border-base-300"></div>
                    </li>
                    <li class="tooltip tooltip-right" data-tip="피드">
                        <a href="/" class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.home()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-right" data-tip="시리즈">
                        <a href="/series" class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/series") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.book()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-right" data-tip="RSS 큐레이션">
                        <a href="/curations" class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/curations") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.news()</div>
                        </a>
                    </li>
                    <li class="tooltip tooltip-right" data-tip="패치노트">
                        <a href="/patch-notes" class="flex gap-4 items-center text-lg p-4 ${isCurrentPath("/patch-notes") ? "text-primary" : "text-base-content"}">
                            <div class="w-6">@template.fragments.svg.info()</div>
                        </a>
                    </li>
                </ul>
            </nav>
            <footer></footer>
        </div>
    </div>
    <div class="w-full sm:min-w-[598px] sm:w-[598px] sm:border-x border-base-300">
        <main>
            ${content}
        </main>
    </div>
    <div class="hidden lg:block h-full w-[259px]">
        ${subContent}
    </div>
</div>
</body>
</html>
