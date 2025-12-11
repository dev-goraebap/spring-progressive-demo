import Alpine from "alpinejs";

Alpine.store('toast', {
    isOpen: false,
    message: null,
    show(message) {
        this.isOpen = true;
        this.message = message;
        setTimeout(() => this.isOpen = false, 2000);
    }
});