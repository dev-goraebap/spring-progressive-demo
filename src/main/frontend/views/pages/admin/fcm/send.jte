@param int totalCount

<form hx-post="/admin/fcm/send"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = "푸시 알림 발송")

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <div class="alert alert-info text-sm">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="stroke-current shrink-0 w-5 h-5">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <span>총 <strong>${totalCount}</strong>명의 구독자에게 발송됩니다.</span>
        </div>

        <div>
            <label class="text-xs text-base-content/60">제목 <span class="text-error">*</span></label>
            <input type="text"
                   name="title"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="알림 제목"
                   required />
        </div>

        <div>
            <label class="text-xs text-base-content/60">내용 <span class="text-error">*</span></label>
            <textarea name="body"
                      rows="3"
                      class="textarea textarea-bordered w-full mt-1"
                      placeholder="알림 내용"
                      required></textarea>
        </div>

        <div>
            <label class="text-xs text-base-content/60">클릭 시 이동 URL</label>
            <input type="text"
                   name="url"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="/posts/xxx (선택사항)" />
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-primary btn-block">
            <span class="htmx-indicator loading loading-spinner loading-sm"></span>
            <span>발송하기</span>
        </button>
    `)
</form>
