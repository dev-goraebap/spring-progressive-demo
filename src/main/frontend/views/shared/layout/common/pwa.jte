<%-- PWA 메타 태그 및 Service Worker 등록 --%>
<link rel="manifest" href="/manifest.json">
<meta name="theme-color" content="#334155">
<meta name="mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="apple-touch-icon" href="/images/logo.png">

<script>
if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('/sw.js');
}
</script>
