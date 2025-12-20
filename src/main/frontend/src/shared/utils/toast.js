import { Notyf } from 'notyf';

let notyf = null;

const getNotyf = () => {
    // 컨테이너가 DOM에 없으면 새 인스턴스 생성
    if (!notyf || !document.body.contains(document.querySelector('.notyf'))) {
        notyf = new Notyf({
            duration: 3000,
            position: { x: 'right', y: 'top' },
            dismissible: true,
            types: [
                {
                    type: 'warning',
                    background: '#f59e0b',
                    icon: false
                }
            ]
        });
    }
    return notyf;
};

export const toast = {
    success(message) {
        getNotyf().success(message);
    },
    warning(message) {
        getNotyf().open({ type: 'warning', message });
    },
    error(message) {
        getNotyf().error(message);
    }
};

window.toast = toast;
