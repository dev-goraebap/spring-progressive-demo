import Alpine from "alpinejs";

// 유효한 테마만 허용, 그 외는 dark로 리셋
const validThemes = ['dark', 'light'];
const savedTheme = document.documentElement.getAttribute('data-theme');
const initialTheme = validThemes.includes(savedTheme) ? savedTheme : 'dark';

// 잘못된 테마가 저장되어 있으면 즉시 수정
if (savedTheme && !validThemes.includes(savedTheme)) {
    document.documentElement.setAttribute('data-theme', initialTheme);
}

Alpine.store('theme', {
    current: initialTheme,

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