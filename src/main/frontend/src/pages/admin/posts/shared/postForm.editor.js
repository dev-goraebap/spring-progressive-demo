import Alpine from 'alpinejs';

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
            thumbnailBlobId: null
        },

        async submit() {
            const editor = window.tinymce?.get('tinymceEditor');
            if (editor) {
                this.form.content = editor.getContent();
            }

            const thumbnailInput = document.getElementById('thumbnailBlobId');
            if (thumbnailInput?.value) {
                this.form.thumbnailBlobId = parseInt(thumbnailInput.value);
            }

            if (!this.form.content.trim()) {
                toast.error('내용을 입력해주세요.');
                return;
            }

            this.submitting = true;
            try {
                const url = isEdit ? `/api/v1/admin/posts/${data.id}` : '/api/v1/admin/posts';
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
                    throw new Error(error.detail || (isEdit ? '게시물 수정에 실패했습니다.' : '게시물 등록에 실패했습니다.'));
                }

                toast.flash('success', isEdit ? '게시물이 수정되었습니다.' : '게시물이 등록되었습니다.');
                window.location.href = '/admin/posts';
            } catch (e) {
                toast.error(e.message);
            } finally {
                this.submitting = false;
            }
        }
    };
});
