@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="ko" data-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="htmx-config" content='{"globalViewTransitions": true}'>
    <title>${title}</title>

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
<body hx-boost="true" class="h-screen">
<div id="htmx-progress"></div>
<div class="h-full flex justify-center gap-4">
    <div class="h-full w-[249px] max-[1300px]:w-fit">
        <div class="py-10 max-[1300px]:hidden">
            <a href="/" class="text-2xl">데브고래밥 블로그</a>
        </div>
        <nav>
            <ul class="flex flex-col">
                <li>
                    <a href="/curations" class="flex gap-4 items-center text-base-content text-lg p-4">
                        <div class="w-6">@template.fragments.icon.rss()</div>
                        <span class="max-[1300px]:hidden">RSS 큐레이션</span>
                    </a>
                </li>
                <li>
                    <a href="/" class="flex gap-4 items-center text-base-content text-lg p-4">
                        <div class="w-6">@template.fragments.icon.home()</div>
                        <span class="max-[1300px]:hidden">피드</span>
                    </a>
                </li>
                <li>
                    <a href="/series" class="flex gap-4 items-center text-base-content text-lg p-4">
                        <div class="w-6">@template.fragments.icon.book()</div>
                        <span class="max-[1300px]:hidden">시리즈</span>
                    </a>
                </li>
                <li>
                    <a href="/patch-notes" class="flex gap-4 items-center text-base-content text-lg p-4">
                        <div class="w-6">@template.fragments.icon.pencil()</div>
                        <span class="max-[1300px]:hidden">패치노트</span>
                    </a>
                </li>
            </ul>
        </nav>
        <footer></footer>
    </div>
    <div class="h-full border-x border-base-300 w-[598px]">
        <main class="h-full">
            ${content}
        </main>
    </div>
    <div class="h-full w-[259px]"></div>
</div>
</body>
</html>
