import Alpine from 'alpinejs';

Alpine.store('modal', {
    isOpen: false,
    closeable: true,
    onResult: null,

    open(options = {}) {
        this.closeable = options.closeable !== false;
        this.onResult = options.onResult || null;
        this.isOpen = true;

        // 스크롤바 숨김
        const scrollbarWidth = window.innerWidth - document.documentElement.clientWidth;
        document.documentElement.style.overflow = 'hidden';
        document.documentElement.style.paddingRight = `${scrollbarWidth}px`;

        // 로딩 표시
        const container = document.getElementById('HX_MODAL_CONTENT');
        if (container) {
            container.innerHTML = document.getElementById('modal-loading').innerHTML;
        }
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
            this.onResult = null;
        }, 200);
    }
});
