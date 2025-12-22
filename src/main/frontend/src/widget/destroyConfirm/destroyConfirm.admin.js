import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('destroyConfirm', (el) => ({
    submitting: false,
    confirmText: '',

    async submit() {
        if (this.confirmText !== '삭제') return;

        this.submitting = true;
        try {
            const res = await fetch(el.dataset.apiUrl, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.detail || el.dataset.errorMessage);
            }

            Alpine.store('modal').onClose();
            toast.defer('success', el.dataset.successMessage);
            htmx.ajax('GET', el.dataset.redirectUrl, { target: 'body', swap: 'outerHTML' });
        } catch (e) {
            toast.error(e.message);
        } finally {
            this.submitting = false;
        }
    }
}));
