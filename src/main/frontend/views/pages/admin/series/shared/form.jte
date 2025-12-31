@param String title
@param String buttonText
@param Long id = null
@param String name = ""
@param String slug = ""
@param String description = ""
@param String status = "PLAN"
@param String isPublishedYn = "N"
@param String publishedAt = ""
@param String thumbnailUrl = null

!{var isEdit = id != null;}
!{var actionUrl = isEdit ? "/admin/series/" + id : "/admin/series";}

<form hx-post="${isEdit ? null : actionUrl}"
      hx-put="${isEdit ? actionUrl : null}"
      hx-disabled-elt="button[type=submit]"
      class="flex flex-col flex-1 min-h-0">
    @template.shared.components.modalHeader(title = title)

    <div class="p-4 flex-1 overflow-y-auto space-y-4">
        <%-- 썸네일 --%>
        <div>
            <label class="text-xs text-base-content/60">썸네일</label>
            <div x-data="filepond('#seriesThumbnailBlobId')" data-thumbnail-url="${thumbnailUrl}" class="mt-1">
                <input type="file" accept="image/*"/>
            </div>
            <input type="hidden" name="thumbnailBlobId" id="seriesThumbnailBlobId"/>
        </div>

        <%-- 시리즈 이름 --%>
        <div>
            <label class="text-xs text-base-content/60">시리즈 이름 <span class="text-error">*</span></label>
            <input type="text"
                   name="name"
                   value="${name}"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="시리즈 이름을 입력하세요"
                   required />
        </div>

        <%-- 설명 --%>
        <div>
            <label class="text-xs text-base-content/60">설명</label>
            <textarea name="description"
                      rows="3"
                      class="textarea textarea-bordered w-full mt-1"
                      placeholder="시리즈에 대한 설명을 입력하세요">${description}</textarea>
        </div>

        <%-- 슬러그 --%>
        <div>
            <label class="text-xs text-base-content/60">슬러그 (URL)</label>
            <input type="text"
                   name="slug"
                   value="${slug}"
                   class="w-full py-2 border-b border-base-300 focus:border-primary focus:outline-none bg-transparent"
                   placeholder="비워두면 자동 생성됩니다" />
        </div>

        <%-- 상태 --%>
        <div>
            <label class="text-xs text-base-content/60">상태</label>
            <select name="status" class="select select-bordered select-sm w-full mt-1">
                @if(status.equals("PLAN"))
                    <option value="PLAN" selected>계획</option>
                @else
                    <option value="PLAN">계획</option>
                @endif
                @if(status.equals("PROGRESS"))
                    <option value="PROGRESS" selected>진행중</option>
                @else
                    <option value="PROGRESS">진행중</option>
                @endif
                @if(status.equals("COMPLETED"))
                    <option value="COMPLETED" selected>완료</option>
                @else
                    <option value="COMPLETED">완료</option>
                @endif
            </select>
        </div>

        <%-- 공개 상태 --%>
        <div>
            <label class="text-xs text-base-content/60">공개 상태</label>
            <select name="isPublishedYn" class="select select-bordered select-sm w-full mt-1">
                @if(isPublishedYn.equals("N"))
                    <option value="N" selected>비공개</option>
                @else
                    <option value="N">비공개</option>
                @endif
                @if(isPublishedYn.equals("Y"))
                    <option value="Y" selected>공개</option>
                @else
                    <option value="Y">공개</option>
                @endif
            </select>
        </div>

        <%-- 발행일 --%>
        <div>
            <label class="text-xs text-base-content/60">발행일</label>
            <input type="datetime-local"
                   name="publishedAt"
                   value="${publishedAt}"
                   class="input input-bordered input-sm w-full mt-1" />
            <p class="text-xs text-base-content/50 mt-1">비워두면 현재 시간으로 설정됩니다</p>
        </div>
    </div>

    @template.shared.components.modalFooter(content = @`
        <button type="submit" class="btn btn-primary btn-block">
            <span class="htmx-indicator loading loading-spinner loading-sm"></span>
            <span>${buttonText}</span>
        </button>
    `)
</form>
