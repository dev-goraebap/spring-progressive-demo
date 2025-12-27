import Alpine from 'alpinejs';
import { getAvatarEmoji } from '@/shared/utils/avatar';

/**
 * 댓글 아이템 아바타 표시 컴포넌트
 *
 * avatarNo를 받아 이모지로 변환하여 표시
 * - 메인: 게시물 상세 댓글 목록
 * - 관리자: 댓글 관리 테이블
 *
 * @example <div x-data="commentItem($el.dataset.avatarNo)"></div>
 */
Alpine.data('commentItem', (avatarNo) => ({
    avatar: getAvatarEmoji(avatarNo)
}));