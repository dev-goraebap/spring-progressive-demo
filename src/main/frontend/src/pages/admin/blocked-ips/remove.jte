@import xyz.goraebap.spring_progressive_demo.infra.view_model.BlockedIpViewModel

@param BlockedIpViewModel blockedIp

<form hx-delete="/admin/blocked-ips/${blockedIp.getId()}"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0 group">
    @template.shared.components.modalHeader(title = "차단 IP 삭제")

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <div class="alert alert-warning">
            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
            </svg>
            <div>
                <p class="font-bold">이 작업은 되돌릴 수 없습니다.</p>
                <p class="text-sm">차단 기록이 완전히 삭제됩니다.</p>
            </div>
        </div>

        <div class="bg-base-200 rounded-lg p-4 space-y-2">
            <div class="flex justify-between">
                <span class="text-base-content/70">IP 주소</span>
                <code class="font-mono">${blockedIp.getIpAddress()}</code>
            </div>
            @if(blockedIp.getReason() != null)
                <div class="flex justify-between">
                    <span class="text-base-content/70">사유</span>
                    <span class="text-right max-w-48 truncate">${blockedIp.getReason()}</span>
                </div>
            @endif
            <div class="flex justify-between">
                <span class="text-base-content/70">차단 경로</span>
                <span>${blockedIp.getBlockedByLabel()}</span>
            </div>
            <div class="flex justify-between">
                <span class="text-base-content/70">상태</span>
                <span>${blockedIp.getStatusLabel()}</span>
            </div>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-error btn-block">
            <span class="loading loading-spinner loading-sm hidden group-[.htmx-request]:inline-flex"></span>
            <span>삭제</span>
        </button>
    `)
</form>
