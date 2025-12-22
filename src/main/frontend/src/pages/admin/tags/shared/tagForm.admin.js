import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('tagForm', (el) => {
    const isEdit = !!el.dataset.id;

    return {
        submitting: false,
        form: {
            name: el.dataset.name || ''
        },

        async submit() {
            if (!this.form.name.trim()) return;

            this.submitting = true;
            try {
                const url = isEdit ? `/api/v1/admin/tags/${el.dataset.id}` : '/api/v1/admin/tags';
                const method = isEdit ? 'PUT' : 'POST';

                const res = await fetch(url, {
                    method,
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    },
                    body: JSON.stringify(this.form)
                });

                if (!res.ok) {
                    const error = await res.json();
                    throw new Error(error.detail || (isEdit ? '태그 수정에 실패했습니다.' : '태그 생성에 실패했습니다.'));
                }

                Alpine.store('modal').onClose();
                toast.defer('success', isEdit ? '태그가 수정되었습니다.' : '태그가 생성되었습니다.');
                htmx.ajax('GET', '/admin/tags', { target: 'body', swap: 'outerHTML' });
            } catch (e) {
                toast.error(e.message);
            } finally {
                this.submitting = false;
            }
        }
    };
});
