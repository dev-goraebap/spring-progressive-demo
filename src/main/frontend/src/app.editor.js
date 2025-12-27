import './app.common.js';
import './app/states/filepond.data';

// editor 전용 JS 로드
import.meta.glob([
    '../views/**/*.editor.js',
], {eager: true});

// TinyMCE
import tinymce from 'tinymce';
import 'tinymce/icons/default';
import 'tinymce/themes/silver';
import 'tinymce/models/dom';

// 플러그인
import 'tinymce/plugins/advlist';
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/link';
import 'tinymce/plugins/image';
import 'tinymce/plugins/charmap';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/anchor';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/code';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/media';
import 'tinymce/plugins/table';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/emoticons';
import 'tinymce/plugins/emoticons/js/emojis';
import 'tinymce/plugins/pagebreak';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/save';
import 'tinymce/plugins/autosave';
import 'tinymce/plugins/directionality';

function initEditor() {
    const editorElement = document.getElementById('tinymceEditor');
    if (!editorElement) return;

    // 기존 에디터 제거
    tinymce.remove('#tinymceEditor');

    // 다크모드 감지
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';

    tinymce.init({
        selector: '#tinymceEditor',
        license_key: 'gpl',
        height: '100%',
        plugins: [
            'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
            'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
            'insertdatetime', 'media', 'table', 'wordcount', 'codesample',
            'emoticons', 'pagebreak', 'nonbreaking', 'save', 'autosave', 'directionality'
        ],
        toolbar1: 'undo redo | bold italic underline strikethrough | fontfamily fontsize blocks | alignleft aligncenter alignright alignjustify',
        toolbar2: 'outdent indent | numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen preview',
        toolbar3: 'image media link anchor codesample | visualblocks code | searchreplace',
        menubar: 'file edit view insert format tools table',
        menu: {
            file: { title: 'File', items: 'newdocument restoredraft | preview' },
            edit: { title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall | searchreplace' },
            view: { title: 'View', items: 'code | visualaid visualchars visualblocks | preview fullscreen' },
            insert: { title: 'Insert', items: 'image link media codesample inserttable | charmap emoticons | pagebreak nonbreaking anchor | insertdatetime' },
            format: { title: 'Format', items: 'bold italic underline strikethrough superscript subscript codeformat | blocks fontfamily fontsize align lineheight | forecolor backcolor | removeformat' },
            tools: { title: 'Tools', items: 'code wordcount' },
            table: { title: 'Table', items: 'inserttable | cell row column | tableprops deletetable' }
        },
        skin: false,
        content_css: '/css/font.css',
        content_style: `
            body {
                font-family: 'Open Sans', 'Gothic A1', sans-serif;
                font-size: 16px;
                line-height: 1.6;
                padding: 1rem;
                background: ${isDark ? '#1d232a' : '#ffffff'};
                color: ${isDark ? '#a6adba' : '#1f2937'};
            }
            h1 { font-size: 2em; font-weight: bold; margin-bottom: 0.5em; }
            h2 { font-size: 1.5em; font-weight: bold; margin-bottom: 0.5em; }
            h3 { font-size: 1.25em; font-weight: bold; margin-bottom: 0.5em; }
            pre {
                background: ${isDark ? '#191e24' : '#f3f4f6'};
                padding: 1rem;
                border-radius: 0.5rem;
                overflow-x: auto;
            }
            code {
                background: ${isDark ? '#191e24' : '#f3f4f6'};
                padding: 0.2rem 0.4rem;
                border-radius: 0.25rem;
                font-size: 0.875em;
            }
            pre code {
                background: transparent;
                padding: 0;
            }
            img { max-width: 100%; height: auto; }
            a { color: #3b82f6; }
            blockquote {
                border-left: 4px solid ${isDark ? '#374151' : '#e5e7eb'};
                padding-left: 1rem;
                margin-left: 0;
                color: ${isDark ? '#9ca3af' : '#6b7280'};
            }
        `,
        codesample_languages: [
            { text: 'HTML/XML', value: 'markup' },
            { text: 'JavaScript', value: 'javascript' },
            { text: 'TypeScript', value: 'typescript' },
            { text: 'CSS', value: 'css' },
            { text: 'Java', value: 'java' },
            { text: 'Kotlin', value: 'kotlin' },
            { text: 'Python', value: 'python' },
            { text: 'Bash', value: 'bash' },
            { text: 'SQL', value: 'sql' },
            { text: 'JSON', value: 'json' },
            { text: 'YAML', value: 'yaml' },
            { text: 'Go', value: 'go' },
            { text: 'Rust', value: 'rust' },
            { text: 'C#', value: 'csharp' },
            { text: 'C++', value: 'cpp' }
        ],
        // 이미지 업로드 설정
        images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
            const formData = new FormData();
            formData.append('file', blobInfo.blob(), blobInfo.filename());

            fetch('/api/v1/admin/media', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) throw new Error('이미지 업로드에 실패했습니다.');
                return response.json();
            })
            .then(data => {
                if (data.status && data.status >= 400) {
                    throw new Error(data.message || '서버 오류가 발생했습니다.');
                }
                resolve(data.url);
            })
            .catch(error => {
                reject({ message: '이미지 업로드에 실패했습니다: ' + error.message, remove: true });
            });
        }),
        automatic_uploads: true,
        paste_data_images: true,
        // 자동저장 설정
        autosave_ask_before_unload: true,
        autosave_interval: '30s',
        autosave_retention: '2m',
        // 기타 설정
        browser_spellcheck: true,
        contextmenu: 'link image table',
        promotion: false,
        branding: false,
        // 단축키 설정
        setup: function(editor) {
            // Ctrl+S로 저장
            editor.addShortcut('ctrl+s', 'Save content', function() {
                document.querySelector('form')?.requestSubmit();
            });
        }
    });
}

// 초기화
initEditor();

// Alpine 시작
Alpine.start();
