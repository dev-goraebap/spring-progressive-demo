@import static xyz.goraebap.blog.shared.jte.JteContext.*

<%-- 쿠키 없을 때 시스템 설정 감지 (FOUC 방지) --%>
@if(getTheme() == null)
<script>
    document.documentElement.setAttribute('data-theme',
        window.matchMedia('(prefers-color-scheme: light)').matches ? 'cupcake' : 'dark');
</script>
@endif
