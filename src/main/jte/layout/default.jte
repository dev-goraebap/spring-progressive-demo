@import static xyz.goraebap.spring_progressive_demo.shared.jte.JteContext.*

@param String title = "Demo"
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="ko" data-theme="cupcake">
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
<body hx-boost="true">
<div id="htmx-progress"></div>
@template.fragments.header()

<main>
    ${content}
</main>

@template.fragments.footer()
</body>
</html>
