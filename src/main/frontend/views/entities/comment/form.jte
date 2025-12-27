@param String postSlug
@param int commentCount
@param String requestId

<div class="space-y-4" x-data="commentForm()">
    <div class="flex justify-between items-center">
        @if(commentCount > 0)
            <span class="font-bold">댓글 ${commentCount}</span>
        @else
            <span class="font-bold">댓글</span>
        @endif
        <span class="text-base-content/70 text-xs">댓글 관련 문의: dev.goraebap@gmail.com</span>
    </div>

    <form action="/comments/${postSlug}"
          method="post"
          hx-post="/comments/${postSlug}"
          hx-target="#HX_COMMENT_SECTION"
          hx-swap="outerHTML"
          class="space-y-4">

        <input type="hidden" name="requestId" value="${requestId}" />
        <input type="hidden" name="avatarNo" x-model="avatarNo" />

        <div class="flex gap-2 items-center">
            <button type="button"
                    class="rounded-full size-10 bg-base-200 text-xl flex items-center justify-center"
                    @click="randomAvatar()">
                <span x-text="avatar"></span>
            </button>

            <label class="input flex-1 flex items-center gap-2">
                <input type="text"
                       name="nickname"
                       x-model="nickname"
                       class="grow"
                       placeholder="닉네임 입력.."
                       maxlength="50"
                       required />
                <button type="button" class="btn btn-sm" @click="randomNickname()">랜덤</button>
            </label>
        </div>

        <textarea name="comment"
                  x-model="comment"
                  @input="checkValid()"
                  placeholder="입력한 댓글은 수정하거나 삭제할 수 없어요. 또한 허위사실, 욕설, 사칭 등 댓글은 통보없이 삭제될 수 있습니다."
                  class="w-full textarea rounded-xl"
                  rows="3"
                  maxlength="1000"
                  required></textarea>

        <div class="text-end">
            <button type="submit"
                    class="btn btn-primary"
                    :disabled="!isValid"
                    :class="{ 'btn-disabled': !isValid }">
                <span class="loading loading-spinner loading-sm hidden [.htmx-request_&]:inline"></span>
                <span class="[.htmx-request_&]:hidden">댓글 남기기</span>
            </button>
        </div>
    </form>
</div>
