@param String title
@param String buttonText
@param Long id = null
@param String name = ""

!{var isEdit = id != null;}
!{var actionUrl = isEdit ? "/admin/tags/" + id : "/admin/tags";}

<form hx-post="${isEdit ? null : actionUrl}"
      hx-put="${isEdit ? actionUrl : null}"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = title)

    <div class="p-4 flex-1 overflow-y-auto">
        <div>
            <label class="text-xs text-base-content/60">태그명 <span class="text-error">*</span></label>
            <input type="text"
                   name="name"
                   value="${name}"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="태그 이름을 입력하세요"
                   required />
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-primary btn-block">
            <span class="htmx-indicator loading loading-spinner loading-sm"></span>
            <span>${buttonText}</span>
        </button>
    `)
</form>
