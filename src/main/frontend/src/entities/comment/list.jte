@import xyz.goraebap.blog.infra.view_model.CommentViewModel
@import java.util.List
@import java.time.format.DateTimeFormatter

@param List<CommentViewModel> comments

!{var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");}

<ul class="mt-6 space-y-4">
    @if(comments.isEmpty())
        <li class="text-center text-base-content/50 py-8">
            아직 댓글이 없습니다. 첫 댓글을 남겨보세요!
        </li>
    @else
        @for(var item : comments)
            <li data-avatar-no="${item.getAvatarNo()}"
                x-data="commentItem($el.dataset.avatarNo)">
                <div class="bg-base-200/70 rounded-xl p-4 space-y-3">
                    <div class="flex justify-between items-start">
                        <div class="flex gap-2 items-center">
                            <div class="rounded-full size-10 bg-base-300 flex justify-center items-center text-xl border border-base-content/30">
                                <span x-text="avatar"></span>
                            </div>
                            <span class="text-sm font-bold">${item.getNickname()}</span>
                        </div>
                        <span class="text-xs text-base-content/70">${item.getCreatedAt().format(formatter)}</span>
                    </div>
                    <p class="text-sm text-base-content/70 break-words whitespace-pre-wrap">${item.getComment()}</p>
                </div>
            </li>
        @endfor
    @endif
</ul>
