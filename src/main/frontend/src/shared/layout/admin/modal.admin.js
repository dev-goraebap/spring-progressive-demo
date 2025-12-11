import Alpine from 'alpinejs';
import htmx from "htmx.org";

Alpine.store('modal', {
    isOpen: false,
    actionUrl: null,
    init() {
        if (this.isOpen) {
            htmx.ajax('GET', this.actionUrl, '#modal-content');
        }
    },
    onOpen(actionUrl) {
        this.isOpen = true;
        this.actionUrl = actionUrl;
    },
    onClose() {
        this.isOpen = false;
        this.actionUrl = null;
    }
});