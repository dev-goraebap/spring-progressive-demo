import Alpine from 'alpinejs';

// 아바타 이모지 목록
const avatars = [
    { icon: '😊', no: 1 },
    { icon: '😂', no: 2 },
    { icon: '😀', no: 3 },
    { icon: '😍', no: 4 },
    { icon: '👾', no: 5 },
    { icon: '😎', no: 6 },
    { icon: '😃', no: 7 },
    { icon: '🤩', no: 8 },
    { icon: '😭', no: 9 },
    { icon: '🥸', no: 10 },
];

// 랜덤 닉네임 생성용 단어
const prefixWords = [
    '귀여운', '멋진', '똑똑한', '재미있는', '훌륭한', '뛰어난', '창의적인', '열정적인',
    '친근한', '활발한', '신비한', '우아한', '당당한', '유쾌한', '차분한', '온화한',
    '진지한', '성실한', '빠른', '느긋한', '꼼꼼한', '대담한', '겸손한', '자신감있는',
    '포근한', '시원한', '따뜻한', '밝은', '조용한', '활기찬'
];

const suffixWords = [
    '개발자', '코딩러', '프로그래머', '엔지니어', '해커', '테크러', '빌더', '메이커',
    '고양이', '강아지', '토끼', '곰', '여우', '다람쥐', '팬더', '코알라',
    '사자', '호랑이', '늑대', '독수리', '부엉이', '펭귄', '돌고래', '거북이',
    '바리스타', '요리사', '작가', '화가', '음악가', '탐험가', '모험가', '여행자'
];

function getRandomAvatar() {
    return avatars[Math.floor(Math.random() * avatars.length)];
}

function getRandomNickname() {
    const prefix = prefixWords[Math.floor(Math.random() * prefixWords.length)];
    const suffix = suffixWords[Math.floor(Math.random() * suffixWords.length)];
    return prefix + suffix;
}

// 댓글 폼 컴포넌트
Alpine.data('commentForm', () => {
    const initialAvatar = getRandomAvatar();
    return {
        avatar: initialAvatar.icon,
        avatarNo: initialAvatar.no,
        nickname: getRandomNickname(),
        comment: '',
        isValid: false,

        randomAvatar() {
            const av = getRandomAvatar();
            this.avatar = av.icon;
            this.avatarNo = av.no;
        },

        randomNickname() {
            this.nickname = getRandomNickname();
        },

        checkValid() {
            this.isValid = this.comment.trim().length > 0;
        }
    };
});

// 댓글 아이템 컴포넌트
Alpine.data('commentItem', (avatarNo) => ({
    avatar: avatars.find(a => a.no === avatarNo)?.icon || '😊'
}));
