@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String entry = null

<%-- 폰트 --%>
<link rel="stylesheet" href="/css/font.css">

@if(isDev())
<%-- 개발: Vite dev server --%>
    <link rel="stylesheet" href="http://localhost:5173/src/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    @if(entry != null)
        <script type="module" src="http://localhost:5173/src/${entry}.js"></script>
    @endif
@else
<%-- 프로덕션: manifest 기반 빌드 파일 --%>
    <link rel="stylesheet" href="/${viteCss()}">
    @if(entry != null)
        <script type="module" src="/${viteJs(entry)}"></script>
    @endif
@endif
