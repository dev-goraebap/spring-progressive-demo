@template.shared.layout.editor(title = "새 포스트 작성", content = @`
    <form action="/admin/posts" method="post" class="flex gap-4 relative">

        <div class="grow flex flex-col p-4 gap-4">
            <textarea id="tinymceEditor" name="content"></textarea>
        </div>

        <div class="min-w-80 flex flex-col justify-between h-screen sticky top-0 border-l border-base-300 p-4">
            <div class="flex flex-col gap-4">
                <%-- 썸네일 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">썸네일</span>
                    </label>
                    <input type="file" id="thumbnailInput" accept="image/*"/>
                    <input type="hidden" name="thumbnailBlobId" id="thumbnailBlobId"/>
                </div>

                <%-- 슬러그 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">슬러그 (URL)</span>
                    </label>
                    <input type="text" name="slug" class="input input-bordered input-sm w-full" placeholder="자동 생성"/>
                    <label class="label">
                        <span class="label-text-alt text-base-content/60">비워두면 자동 생성됩니다</span>
                    </label>
                </div>

                <%-- 포스트 타입 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">포스트 타입</span>
                    </label>
                    <select name="postType" class="select select-bordered select-sm w-full">
                        <option value="post" selected>일반 포스트</option>
                        <option value="patch-note">패치노트</option>
                    </select>
                </div>

                <%-- 공개 상태 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">공개 상태</span>
                    </label>
                    <select name="isPublishedYn" class="select select-bordered select-sm w-full">
                        <option value="N" selected>비공개</option>
                        <option value="Y">공개</option>
                    </select>
                </div>

                <%-- 발행일 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">발행일</span>
                    </label>
                    <input type="datetime-local" name="publishedAt" class="input input-bordered input-sm w-full"/>
                    <label class="label">
                        <span class="label-text-alt text-base-content/60">비워두면 현재 시간으로 설정됩니다</span>
                    </label>
                </div>
            </div>

            <div class="flex gap-2">
                <a href="/admin/posts" class="btn btn-ghost flex-1">작업 취소</a>
                <button type="submit" class="btn btn-primary flex-1">저장</button>
            </div>
        </div>
    </form>
`)
