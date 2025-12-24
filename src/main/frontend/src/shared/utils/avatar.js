// 아바타 이모지 목록 (동물 테마)
export const AVATARS = [
    '🐶', '🐱', '🐭', '🐹', '🐰', '🦊', '🐻', '🐼', '🐨', '🐯', '🦁', '🐮', '🐷', '🐸', '🐵'
];

/**
 * avatarNo를 이모지로 변환
 * @param {number|string} avatarNo - 아바타 번호 (0-14)
 * @returns {string} 아바타 이모지
 */
export function getAvatarEmoji(avatarNo) {
    const no = Number(avatarNo);
    if (isNaN(no) || no < 0 || no >= AVATARS.length) {
        return AVATARS[0]; // 기본값: 강아지
    }
    return AVATARS[no];
}

/**
 * 랜덤 아바타 반환
 * @returns {{ icon: string, no: number }}
 */
export function getRandomAvatar() {
    const no = Math.floor(Math.random() * AVATARS.length);
    return { icon: AVATARS[no], no };
}
