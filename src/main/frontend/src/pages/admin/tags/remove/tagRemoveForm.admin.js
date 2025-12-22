import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('tagRemoveForm', (el) => ({
    submitting: false,
    confirmText: '',
    id: el.dataset.id,

    async submit() {
        if (this.confirmText !== '삭제') return;

        this.submitting = true;
        try {
            const res = await fetch(`/api/v1/admin/tags/${this.id}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.detail || '태그 삭제에 실패했습니다.');
            }

            Alpine.store('modal').onClose();
            toast.defer('success', '태그가 삭제되었습니다.');
            htmx.ajax('GET', '/admin/tags', { target: 'body', swap: 'outerHTML' });
        } catch (e) {
            toast.error(e.message);
        } finally {
            this.submitting = false;
        }
    }
}));
