@import xyz.goraebap.spring_progressive_demo.infra.view_model.AdminPostViewModel
@import java.time.format.DateTimeFormatter

@param AdminPostViewModel post

!{var dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");}

@template.shared.layout.editor(title = "포스트 수정", content = @`
    <form action="/admin/posts/${post.getId()}" method="post" class="flex gap-4 relative">
        <input type="hidden" name="_method" value="PUT"/>

        <div class="grow flex flex-col p-4 gap-4">
            <textarea id="tinymceEditor" name="content">${post.getContent()}</textarea>
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
                    @if(post.getThumbnailUrl() != null)
                        <div id="currentThumbnail" class="mt-2">
                            <img src="${post.getThumbnailUrl()}" alt="현재 썸네일" class="w-full max-h-40 object-cover rounded"/>
                            <p class="text-xs text-base-content/50 mt-1">새 이미지를 업로드하면 교체됩니다</p>
                        </div>
                    @endif
                </div>

                <%-- 슬러그 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">슬러그 (URL)</span>
                    </label>
                    <input type="text" name="slug" value="${post.getSlug()}" class="input input-bordered input-sm w-full" placeholder="자동 생성"/>
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
                        @if(post.getPostType().equals("post"))
                            <option value="post" selected>일반 포스트</option>
                            <option value="patch-note">패치노트</option>
                        @else
                            <option value="post">일반 포스트</option>
                            <option value="patch-note" selected>패치노트</option>
                        @endif
                    </select>
                </div>

                <%-- 공개 상태 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">공개 상태</span>
                    </label>
                    <select name="isPublishedYn" class="select select-bordered select-sm w-full">
                        @if(post.getIsPublishedYn().equals("Y"))
                            <option value="N">비공개</option>
                            <option value="Y" selected>공개</option>
                        @else
                            <option value="N" selected>비공개</option>
                            <option value="Y">공개</option>
                        @endif
                    </select>
                </div>

                <%-- 발행일 --%>
                <div class="form-control">
                    <label class="label">
                        <span class="label-text">발행일</span>
                    </label>
                    <input type="datetime-local" name="publishedAt" value="${post.getPublishedAt() != null ? post.getPublishedAt().format(dateTimeFormat) : ""}" class="input input-bordered input-sm w-full"/>
                    <label class="label">
                        <span class="label-text-alt text-base-content/60">비워두면 현재 시간으로 설정됩니다</span>
                    </label>
                </div>

                <%-- 정보 --%>
                <div class="card bg-base-200">
                    <div class="card-body p-4">
                        <h3 class="card-title text-sm">정보</h3>
                        <div class="text-sm space-y-1 text-base-content/70">
                            <p>조회수: ${post.getViewCount()}</p>
                            @if(post.getTags() != null && !post.getTags().isEmpty())
                                <p>태그: ${post.getTags()}</p>
                            @endif
                        </div>
                    </div>
                </div>
            </div>

            <div class="flex gap-2">
                <a href="/admin/posts" class="btn btn-ghost flex-1">작업 취소</a>
                <button type="submit" class="btn btn-primary flex-1">저장</button>
            </div>
        </div>
    </form>
`)
