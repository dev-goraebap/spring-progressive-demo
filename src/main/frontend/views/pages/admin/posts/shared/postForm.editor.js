import Alpine from 'alpinejs';
import htmx from 'htmx.org';

Alpine.data('postForm', (el) => {
    const data = el.dataset.form ? JSON.parse(el.dataset.form) : {};
    const isEdit = !!data.id;
    const publishedAt = data.publishedAt ? data.publishedAt.substring(0, 16) : '';

    return {
        submitting: false,
        form: {
            slug: data.slug || '',
            content: '',
            postType: data.postType || 'post',
            isPublishedYn: data.isPublishedYn || 'N',
            publishedAt,
            thumbnailBlobId: null,
            clearThumbnail: false
        },

        submit() {
            const editor = window.tinymce?.get('tinymceEditor');
            if (editor) {
                this.form.content = editor.getContent();
            }

            const thumbnailInput = document.getElementById('thumbnailBlobId');
            if (thumbnailInput?.value) {
                const parsed = parseInt(thumbnailInput.value);
                if (!isNaN(parsed)) {
                    this.form.thumbnailBlobId = parsed;
                    this.form.clearThumbnail = false;
                }
            } else if (data.thumbnailUrl && thumbnailInput?.dataset.removed === 'true') {
                // 기존에 썸네일이 있었는데 명시적으로 제거된 경우
                this.form.clearThumbnail = true;
            }

            if (!this.form.content.trim()) {
                toast.error('내용을 입력해주세요.');
                return;
            }

            this.submitting = true;

            const url = isEdit ? `/admin/posts/${data.id}` : '/admin/posts';
            const method = isEdit ? 'PUT' : 'POST';

            // null 값 제거
            const values = Object.fromEntries(
                Object.entries(this.form).filter(([_, v]) => v != null)
            );

            htmx.ajax(method, url, {
                values,
                swap: 'none'
            }).finally(() => {
                this.submitting = false;
            });
        }
    };
});
