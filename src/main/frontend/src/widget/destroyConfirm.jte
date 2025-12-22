@param String title
@param String targetName
@param String targetType
@param String apiUrl
@param String successMessage
@param String errorMessage
@param String redirectUrl

<div x-data="destroyConfirm($el)"
     data-api-url="${apiUrl}"
     data-success-message="${successMessage}"
     data-error-message="${errorMessage}"
     data-redirect-url="${redirectUrl}"
     class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = title)

    <div class="p-4 flex-1 overflow-y-auto">
        <div class="text-center mb-4">
            <p class="text-base-content/70">정말 <strong class="text-error">${targetName}</strong> ${targetType}을(를) 삭제하시겠습니까?</p>
            <p class="text-sm text-base-content/50 mt-1">이 작업은 되돌릴 수 없습니다.</p>
        </div>
        <div>
            <label class="text-xs text-base-content/60">확인을 위해 <strong class="text-error">삭제</strong>를 입력하세요</label>
            <input type="text"
                   x-model="confirmText"
                   @keydown.enter.prevent="submit()"
                   class="w-full py-2 border-b border-base-300 focus:border-error focus:outline-none bg-transparent"
                   placeholder="삭제" />
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="button" @click="submit()" :disabled="submitting || confirmText !== '삭제'" class="btn btn-error btn-block">
            <span x-show="!submitting">삭제</span>
            <span x-show="submitting" class="loading loading-spinner loading-sm"></span>
        </button>
    `)
</div>
