@param Long commentId

<form hx-delete="/admin/comments/${commentId}"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = "댓글 삭제")

    <div class="p-4 flex-1 overflow-y-auto">
        <div class="alert alert-warning">
            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
            </svg>
            <span>정책을 위반한 댓글을 삭제합니다. 이 작업은 되돌릴 수 없습니다.</span>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-error btn-block">
            <span class="loading loading-spinner loading-sm hidden group-[.htmx-request]:inline-flex"></span>
            <span>삭제</span>
        </button>
    `)
</form>
