import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('tagEditForm', (el) => ({
    submitting: false,
    id: el.dataset.id,
    form: { name: el.dataset.name },

    async submit() {
        if (!this.form.name.trim()) return;

        this.submitting = true;
        try {
            const res = await fetch(`/api/v1/admin/tags/${this.id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(this.form)
            });

            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.detail || '태그 수정에 실패했습니다.');
            }

            Alpine.store('modal').onClose();
            toast.defer('success', '태그가 수정되었습니다.');
            htmx.ajax('GET', '/admin/tags', { target: 'body', swap: 'outerHTML' });
        } catch (e) {
            toast.error(e.message);
        } finally {
            this.submitting = false;
        }
    }
}));
