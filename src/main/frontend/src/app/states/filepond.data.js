import Alpine from 'alpinejs';
import { createFilePond, destroyFilePond } from '@/shared/libs/filepond';

/**
 * FilePond 업로더 공용 컴포넌트
 *
 * @param {string} targetSelector - 업로드 완료 시 blobId를 저장할 hidden input 셀렉터
 *
 * @example
 * <div x-data="filepond('#thumbnailBlobId')" data-thumbnail-url="${thumbnailUrl}">
 *     <input type="file" accept="image/*"/>
 * </div>
 * <input type="hidden" id="thumbnailBlobId"/>
 */
Alpine.data('filepond', (targetSelector) => ({
    pond: null,

    init() {
        const input = this.$el.querySelector('input[type="file"]');
        if (!input || input._filepond) return;
        input._filepond = true;

        const thumbnailUrl = this.$el.dataset.thumbnailUrl;
        const options = {
            maxFiles: 1,
            labelIdle: '이미지를 드래그하거나 <span class="filepond--label-action">클릭</span>',
            onprocessfile: (error, fileItem) => {
                if (!error && fileItem.serverId && !isNaN(parseInt(fileItem.serverId))) {
                    document.querySelector(targetSelector).value = fileItem.serverId;
                }
            },
            onremovefile: () => {
                document.querySelector(targetSelector).value = '';
            }
        };

        if (thumbnailUrl) {
            options.files = [{
                source: thumbnailUrl,
                options: { type: 'limbo', metadata: { poster: thumbnailUrl } }
            }];
        }

        this.pond = createFilePond(input, options);
    },

    destroy() {
        if (this.pond) destroyFilePond(this.pond);
    }
}));
