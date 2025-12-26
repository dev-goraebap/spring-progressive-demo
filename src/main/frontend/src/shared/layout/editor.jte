@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String title = "에디터"
@param gg.jte.Content content

!{var siteName = "데브고래밥 스튜디오";}

<!DOCTYPE html>
<html lang="ko" data-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>${siteName} | ${title}</title>
    <link rel="icon" type="image/png" href="/images/logo.png">

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
    <link rel="stylesheet" href="http://localhost:5173/src/app/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    <script type="module" src="http://localhost:5173/src/app/app.editor.js"></script>
    @else
    <!-- 프로덕션: manifest 기반 빌드 파일 -->
    <link rel="stylesheet" href="/${viteCss()}">
    <script type="module" src="/${viteJs("editor")}"></script>
    @endif
</head>
<body class="bg-base-100">
    <div class="container max-w-6xl mx-auto">
        ${content}
    </div>
</body>
</html>
