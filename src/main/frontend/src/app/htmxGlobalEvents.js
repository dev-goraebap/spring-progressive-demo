import Alpine from "alpinejs";

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
 * [Fallback] HX-Trigger가 없는 에러 응답 처리
 *
 * 케이스:
 * - Spring 기본 404 (존재하지 않는 URL)
 * - GlobalExceptionHandler를 거치지 않는 에러
 *
 * 동작: 응답 body에서 에러 메시지 추출하여 toast 표시
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
 * [HX-Trigger] toast 이벤트 핸들러
 *
 * 케이스:
 * - 성공 응답 (CRUD 완료)
 * - DTO validation 실패 (400)
 * - Service 예외 (NotFoundException, BadRequestException 등)
 *
 * 동작: htmx가 HX-Trigger 헤더를 파싱하여 toast 이벤트 발생 → 즉시 표시
 */
document.body.addEventListener('toast', (e) => {
    const {type, message} = e.detail;
    window.toast[type](decodeURIComponent(message));
});

/**
 * [HX-Trigger + 페이지 전환] toast를 flash로 저장
 *
 * 케이스:
 * - HX-Location 응답 (모달 CRUD 후 목록 갱신)
 * - HX-Redirect 응답 (레이아웃이 다른 페이지로 이동)
 *
 * 동작: 페이지가 교체되므로 sessionStorage에 저장, 새 페이지에서 표시
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
                const {type, message} = events.toast;
                console.debug('[htmx] toast.flash:', type, decodeURIComponent(message));
                window.toast.flash(type, decodeURIComponent(message));
            }
        } catch (err) {
            console.error('[htmx] Failed to parse HX-Trigger', err);
        }
    }
});

/**
 * [HX-Trigger] closeModal 이벤트 핸들러
 *
 * 케이스:
 * - 모달 내 CRUD 성공 후 모달 닫기
 *
 * 동작: Alpine.js 모달 스토어의 onClose 호출
 */
document.body.addEventListener('closeModal', () => {
    Alpine.store('modal').onClose();
});
