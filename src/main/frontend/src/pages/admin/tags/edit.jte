@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminTagViewModel

@param AdminTagViewModel tag

<div x-data="tagEditForm($el)"
     data-id="${tag.getId()}"
     data-name="${tag.getName()}"
     class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = "태그 수정")

    <%-- 폼 --%>
    <div class="p-4 flex-1 overflow-y-auto">
        <div>
            <label class="text-xs text-base-content/60">태그명 <span class="text-error">*</span></label>
            <input type="text"
                   x-model="form.name"
                   @keydown.enter.prevent="submit()"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="태그 이름을 입력하세요"
                   required/>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="button" @click="submit()" :disabled="submitting || !form.name.trim()"
                class="btn btn-primary btn-block">
            <span x-show="!submitting">수정</span>
            <span x-show="submitting" class="loading loading-spinner loading-sm"></span>
        </button>
    `)
</div>
