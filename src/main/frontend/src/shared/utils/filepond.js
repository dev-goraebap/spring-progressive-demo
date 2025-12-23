import * as FilePond from 'filepond';
import FilePondPluginFileValidateSize from 'filepond-plugin-file-validate-size';
import FilePondPluginFileValidateType from 'filepond-plugin-file-validate-type';
import FilePondPluginImagePreview from 'filepond-plugin-image-preview';
import FilePondPluginImageValidateSize from 'filepond-plugin-image-validate-size';

// 플러그인 등록 (한 번만 실행)
let pluginsRegistered = false;

function registerPlugins() {
    if (pluginsRegistered) return;

    FilePond.registerPlugin(
        FilePondPluginFileValidateSize,
        FilePondPluginFileValidateType,
        FilePondPluginImagePreview,
        FilePondPluginImageValidateSize
    );

    pluginsRegistered = true;
}

/**
 * FilePond 인스턴스 생성
 * @param {HTMLElement} element - input[type="file"] 요소
 * @param {Object} options - 추가 옵션
 * @returns {FilePond.FilePond} FilePond 인스턴스
 */
export function createFilePond(element, options = {}) {
    registerPlugins();

    const defaultOptions = {
        acceptedFileTypes: ['image/*'],
        server: {
            process: {
                url: '/api/v1/admin/media',
                method: 'POST',
                withCredentials: false,
                headers: {},
                timeout: 7000,
                onload: (response) => {
                    const result = JSON.parse(response);
                    return result.blobId;
                },
                onerror: (response) => {
                    console.error('FilePond 업로드 실패:', response);
                    return response;
                },
            },
            revert: null,
            restore: null,
            load: null,
        },
        name: 'file',
        labelIdle: '파일을 드래그하거나 <span class="filepond--label-action">클릭</span>하세요',
    };

    return FilePond.create(element, { ...defaultOptions, ...options });
}

/**
 * FilePond 인스턴스 제거
 * @param {FilePond.FilePond} pond - FilePond 인스턴스
 */
export function destroyFilePond(pond) {
    if (pond) {
        pond.destroy();
    }
}

export { FilePond };
