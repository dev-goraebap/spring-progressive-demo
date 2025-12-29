@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String entry = null

<%-- 폰트 --%>
<link rel="stylesheet" href="/css/font.css">

<%-- Vite 빌드 파일 --%>
<link rel="stylesheet" href="/builds/style.css?v=${version()}">
@if(entry != null)
    <script type="module" src="/builds/${entry}.js?v=${version()}"></script>
@endif
