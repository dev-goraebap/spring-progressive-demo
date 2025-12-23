import Alpine from "alpinejs";
import htmx from "htmx.org";

/**
 * 프로그레스바 UI
 * 페이지 전환시 오래 걸리는 작업에 대해서 상단 프로그래스바 UI 노출
 */
window.addEventListener('htmx:beforeRequest', () => {
    document.documentElement.classList.remove('htmx-request-complete');
    document.documentElement.classList.add('htmx-request');
});

window.addEventListener('htmx:afterRequest', () => {
    document.documentElement.classList.remove('htmx-request');
    document.documentElement.classList.add('htmx-request-complete');
    setTimeout(() => {
        document.documentElement.classList.remove('htmx-request-complete');
    }, 300);
});

/**
 * 에러 응답에서 HX-Trigger가 없을 때 기본 에러 메시지 표시
 */
document.body.addEventListener('htmx:afterRequest', (e) => {
    const xhr = e.detail.xhr;
    if (!xhr || xhr.status < 400) return;

    // HX-Trigger가 있으면 htmx가 자동 처리하므로 스킵
    const trigger = xhr.getResponseHeader('HX-Trigger');
    if (trigger) return;

    // HX-Trigger가 없으면 응답 body에서 에러 메시지 추출
    try {
        const body = JSON.parse(xhr.responseText);
        window.toast.error(body.detail || body.message || '요청 처리 중 오류가 발생했습니다.');
    } catch {
        window.toast.error('요청 처리 중 오류가 발생했습니다.');
    }
});

/**
 * HX-Trigger 이벤트 핸들러
 * 서버에서 HX-Trigger 헤더로 전달한 이벤트 처리
 */
document.body.addEventListener('toast', (e) => {
    const { type, message } = e.detail;
    window.toast[type](decodeURIComponent(message));
});

/**
 * HX-Location/HX-Redirect + HX-Trigger 동시 처리
 * 페이지가 교체되므로 toast를 flash로 저장
 */
document.body.addEventListener('htmx:afterRequest', (e) => {
    const xhr = e.detail.xhr;
    if (!xhr) return;

    const location = xhr.getResponseHeader('HX-Location');
    const redirect = xhr.getResponseHeader('HX-Redirect');
    const trigger = xhr.getResponseHeader('HX-Trigger');
    console.debug('[htmx] HX-Location:', location, 'HX-Redirect:', redirect, 'HX-Trigger:', trigger);

    if ((location || redirect) && trigger) {
        try {
            const events = JSON.parse(trigger);
            if (events.toast) {
                const { type, message } = events.toast;
                console.debug('[htmx] toast.flash:', type, decodeURIComponent(message));
                window.toast.flash(type, decodeURIComponent(message));
            }
        } catch (err) {
            console.error('[htmx] Failed to parse HX-Trigger', err);
        }
    }
});

document.body.addEventListener('closeModal', () => {
    setTimeout(() => Alpine.store('modal').onClose(), 0);
});