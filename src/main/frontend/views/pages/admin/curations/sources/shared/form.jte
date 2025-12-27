@param String title
@param String buttonText
@param Long id = null
@param String name = ""
@param String url = ""
@param String isActiveYn = "Y"

!{var isEdit = id != null;}
!{var actionUrl = isEdit ? "/admin/curations/sources/" + id : "/admin/curations/sources";}

<form hx-post="${isEdit ? null : actionUrl}"
      hx-put="${isEdit ? actionUrl : null}"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = title)

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <%-- 소스 이름 --%>
        <div>
            <label class="text-xs text-base-content/60">소스 이름 <span class="text-error">*</span></label>
            <input type="text"
                   name="name"
                   value="${name}"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="소스 이름을 입력하세요"
                   required />
        </div>

        <%-- RSS URL --%>
        <div>
            <label class="text-xs text-base-content/60">RSS URL <span class="text-error">*</span></label>
            <input type="url"
                   name="url"
                   value="${url}"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="https://example.com/feed.xml"
                   required />
        </div>

        <%-- 활성 상태 --%>
        <div>
            <label class="text-xs text-base-content/60">활성 상태</label>
            <select name="isActiveYn" class="select select-bordered select-sm w-full mt-1">
                @if(isActiveYn.equals("Y"))
                    <option value="Y" selected>활성</option>
                @else
                    <option value="Y">활성</option>
                @endif
                @if(isActiveYn.equals("N"))
                    <option value="N" selected>비활성</option>
                @else
                    <option value="N">비활성</option>
                @endif
            </select>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-primary btn-block">
            <span class="htmx-indicator loading loading-spinner loading-sm"></span>
            <span>${buttonText}</span>
        </button>
    `)
</form>
