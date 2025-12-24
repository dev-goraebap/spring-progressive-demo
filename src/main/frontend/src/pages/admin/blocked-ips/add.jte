<form hx-post="/admin/blocked-ips"
      hx-swap="none"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0 group">
    @template.shared.components.modalHeader(title = "IP 차단 등록")

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <div class="form-control">
            <label class="label">
                <span class="label-text">IP 주소 <span class="text-error">*</span></span>
            </label>
            <input type="text"
                   name="ipAddress"
                   placeholder="예: 192.168.1.1"
                   class="input input-bordered w-full"
                   required/>
        </div>

        <div class="form-control">
            <label class="label">
                <span class="label-text">차단 사유</span>
            </label>
            <textarea name="reason"
                      placeholder="차단 사유를 입력하세요 (선택)"
                      class="textarea textarea-bordered w-full h-24"></textarea>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-error btn-block">
            <span class="loading loading-spinner loading-sm hidden group-[.htmx-request]:inline-flex"></span>
            <span>차단</span>
        </button>
    `)
</form>
