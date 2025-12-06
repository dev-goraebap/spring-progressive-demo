import Alpine from "alpinejs";

Alpine.store('theme', {
    current: document.documentElement.getAttribute('data-theme') || 'dark',

    toggle() {
        this.current = this.current === 'dark' ? 'cupcake' : 'dark';
        document.documentElement.setAttribute('data-theme', this.current);
        this.setCookie('theme', this.current, 365);
    },

    setCookie(name, value, days) {
        const expires = new Date(Date.now() + days * 864e5).toUTCString();
        document.cookie = `${name}=${value}; expires=${expires}; path=/`;
    }
});