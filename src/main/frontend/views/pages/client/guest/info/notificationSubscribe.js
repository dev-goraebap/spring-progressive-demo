import Alpine from 'alpinejs';

Alpine.data('notificationSubscribe', () => ({
    loading: false,

    async subscribe() {
        this.loading = true;
        try {
            const permission = await Notification.requestPermission();

            if (permission === 'granted') {
                await this.$store.notification.registerToken();
                this.$store.notification.permission = 'granted';
                window.toast?.success('알림이 설정되었습니다!');
            } else {
                this.$store.notification.permission = permission;
            }

            htmx.ajax('GET', '/guest/info?permission=' + permission, '#HX_MODAL_CONTENT');
        } finally {
            this.loading = false;
        }
    }
}));
