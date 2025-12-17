import Alpine from 'alpinejs';

// Easter Egg: 프로필 이미지 5번 연속 클릭 시 /admin 이동
Alpine.data('easterEgg', () => ({
    clicks: 0,
    timer: null,

    handleClick() {
        this.clicks++;
        clearTimeout(this.timer);

        if (this.clicks >= 5) {
            this.clicks = 0;
            window.location.href = '/admin/posts';
        } else {
            this.timer = setTimeout(() => {
                this.clicks = 0;
            }, 1000);
        }
    }
}));
