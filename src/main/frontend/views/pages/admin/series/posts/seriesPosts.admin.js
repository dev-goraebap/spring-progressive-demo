import Alpine from 'alpinejs';

Alpine.data('seriesPostsManager', () => ({
    changed: false,
    sortable: null,

    init() {
        this.initSortable();
    },

    initSortable() {
        const el = this.$refs.sortableList;
        if (!el) return;

        this.sortable = new Sortable(el, {
            animation: 150,
            handle: '.drag-handle',
            onEnd: () => {
                this.changed = true;
                this.updateOrders();
            }
        });
    },

    updateOrders() {
        const items = this.$refs.sortableList.querySelectorAll('[data-id]');
        const orders = Array.from(items).map((el, idx) => ({
            id: parseInt(el.dataset.id),
            sortOrder: idx
        }));
        this.$refs.ordersInput.value = JSON.stringify({ items: orders });
    },

    onOrdersSaved(event) {
        if (event.detail.successful) {
            this.changed = false;
        }
    }
}));
