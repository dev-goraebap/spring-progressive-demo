import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('tagForm', () => ({
    submitting: false,
    form: {
        name: ''
    },

    async submit() {
        if (!this.form.name.trim()) return;

        this.submitting = true;
        try {
            const res = await fetch('/api/v1/admin/tags', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.form)
            });

            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.message || '태그 생성에 실패했습니다.');
            }

            toast.success('태그가 생성되었습니다.');
            Alpine.store('modal').onClose();
            htmx.ajax('GET', '/admin/tags', { target: 'body', swap: 'outerHTML' });
        } catch (e) {
            toast.error(e.message);
        } finally {
            this.submitting = false;
        }
    }
}));
