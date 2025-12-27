import Alpine from 'alpinejs';
import { getAvatarEmoji } from '@/shared/utils/avatar';

// 댓글 아이템 컴포넌트
Alpine.data('commentItem', (avatarNo) => ({
    avatar: getAvatarEmoji(avatarNo)
}));