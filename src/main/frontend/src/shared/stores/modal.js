import Alpine from 'alpinejs';
import htmx from "htmx.org";

Alpine.store('modal', {
    isOpen: false,
    url: null,
    closeable: true,
    onResult: null,

    onOpen(url, options = {}) {
        this.url = url;
        this.closeable = options.closeable !== false;
        this.onResult = options.onResult || null;
        this.isOpen = true;

        // 스크롤바 숨김
        const scrollbarWidth = window.innerWidth - document.documentElement.clientWidth;
        document.documentElement.style.overflow = 'hidden';
        document.documentElement.style.paddingRight = `${scrollbarWidth}px`;

        // 콘텐츠 로드
        setTimeout(() => {
            const container = document.getElementById('modal-content');
            if (container) {
                container.innerHTML = document.getElementById('modal-loading').innerHTML;
                htmx.ajax('GET', url, '#modal-content');
            }
        }, 0);
    },

    onClose(result) {
        this.isOpen = false;

        // 콜백 실행
        if (this.onResult && result !== undefined) {
            this.onResult(result);
        }

        // 스크롤바 복원
        setTimeout(() => {
            document.documentElement.style.paddingRight = '';
            document.documentElement.style.overflow = '';
            this.url = null;
            this.onResult = null;
        }, 200);
    }
});
