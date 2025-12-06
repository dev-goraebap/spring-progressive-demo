import Alpine from "alpinejs";

Alpine.store('theme', {
    current: document.documentElement.getAttribute('data-theme') || 'dark',

    toggle() {
        // 트랜지션 클래스 추가
        document.documentElement.classList.add('theme-transitioning');

        this.current = this.current === 'dark' ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', this.current);
        this.setCookie('theme', this.current, 365);

        // 트랜지션 완료 후 클래스 제거 (300ms)
        setTimeout(() => {
            document.documentElement.classList.remove('theme-transitioning');
        }, 300);
    },

    setCookie(name, value, days) {
        const expires = new Date(Date.now() + days * 864e5).toUTCString();
        document.cookie = `${name}=${value}; expires=${expires}; path=/`;
    }
});