import Alpine from 'alpinejs';

// BGM 플레이리스트 (페이지 로드 시 셔플)
function shuffleArray(array) {
    const shuffled = [...array];
    for (let i = shuffled.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
}

const originalPlaylist = [
    { title: 'Jane Doe', artist: '요네즈 켄시 와 우타다 히카루', src: '/bgm/bgm01.mp3', cover: '/bgm/bgm01.webp' },
    { title: '34+35 slow (guitar)', artist: 'Ariana grande (cover)', src: '/bgm/bgm02.mp3', cover: '/bgm/bgm02.webp' },
    { title: 'Eternal Anamnesis', artist: 'Genshin Impact Main Theme', src: '/bgm/bgm03.mp3', cover: '/bgm/bgm03.jpg' },
    { title: 'Reminiscence', artist: '테일즈위버', src: '/bgm/bgm04-1.mp3', cover: '/bgm/bgm04-1.jpg' },
    { title: '어릴 적 할머니가 들려주신 옛 전설', artist: '마비노기', src: '/bgm/bgm04-2.mp3', cover: '/bgm/bgm04-2.jpg' },
    { title: '나비보벳따우', artist: '동물의숲', src: '/bgm/bgm04-3.mp3', cover: '/bgm/bgm04-3.jpg' },
    { title: 'Dear', artist: 'Nanase', src: '/bgm/bgm05.mp3', cover: '/bgm/bgm05.png' },
];

// 페이지 로드 시 한 번만 셔플 (페이지 이동해도 유지)
if (!window.bgmPlaylist) {
    window.bgmPlaylist = shuffleArray(originalPlaylist);
}
const playlist = window.bgmPlaylist;

// Audio 객체 전역 유지 (페이지 이동해도 끊기지 않게)
if (!window.bgmAudio) {
    window.bgmAudio = new Audio();
    window.bgmAudio.volume = 0.3;
}

// 첫 클릭 시 자동 재생
if (!window.bgmAutoPlayRegistered) {
    window.bgmAutoPlayRegistered = true;
    const autoPlay = () => {
        const audio = window.bgmAudio;
        if (!audio.src) {
            audio.src = playlist[0].src;
        }
        if (audio.paused) {
            audio.play().catch(() => {});
        }
        document.removeEventListener('click', autoPlay);
    };
    document.addEventListener('click', autoPlay);
}

// 시간 포맷
function formatTime(seconds) {
    if (isNaN(seconds)) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

// 회전 각도 전역 유지
if (typeof window.bgmRotation === 'undefined') {
    window.bgmRotation = 0;
}

// BGM Alpine Store 등록
Alpine.store('bgm', {
    isPlaying: false,
    currentIndex: 0,
    currentTime: '0:00',
    duration: '0:00',
    progress: 0,
    volume: 30,
    isMuted: false,
    rotation: window.bgmRotation,

    init() {
        const audio = window.bgmAudio;

        // 회전 애니메이션 시작
        this._startRotation();

        audio.addEventListener('play', () => {
            this.isPlaying = true;
        });

        audio.addEventListener('pause', () => {
            this.isPlaying = false;
        });

        audio.addEventListener('ended', () => {
            this.next(true);
        });

        audio.addEventListener('timeupdate', () => {
            if (audio.duration) {
                this.progress = (audio.currentTime / audio.duration) * 100;
                this.currentTime = formatTime(audio.currentTime);
            }
        });

        audio.addEventListener('loadedmetadata', () => {
            this.duration = formatTime(audio.duration);
        });

        // 기존 상태 복원
        this.isPlaying = !audio.paused;
        this.volume = audio.volume * 100;
    },

    get track() {
        return playlist[this.currentIndex];
    },

    load(index) {
        this.currentIndex = index;
        window.bgmAudio.src = playlist[index].src;
    },

    toggle() {
        const audio = window.bgmAudio;
        if (this.isPlaying) {
            audio.pause();
        } else {
            if (!audio.src) this.load(0);
            audio.play();
        }
    },

    prev() {
        let newIndex = this.currentIndex - 1;
        if (newIndex < 0) newIndex = playlist.length - 1;
        this.load(newIndex);
        if (this.isPlaying) window.bgmAudio.play();
    },

    next(autoPlay = false) {
        let newIndex = this.currentIndex + 1;
        if (newIndex >= playlist.length) newIndex = 0;
        this.load(newIndex);
        if (this.isPlaying || autoPlay) window.bgmAudio.play();
    },

    seek(value) {
        const audio = window.bgmAudio;
        if (audio.duration) {
            audio.currentTime = (value / 100) * audio.duration;
        }
    },

    setVolume(value) {
        this.volume = value;
        window.bgmAudio.volume = value / 100;
    },

    toggleMute() {
        this.isMuted = !this.isMuted;
        window.bgmAudio.muted = this.isMuted;
    },

    _startRotation() {
        if (window.bgmRotationStarted) return;
        window.bgmRotationStarted = true;

        const animate = () => {
            if (this.isPlaying) {
                window.bgmRotation += 0.5; // 속도 조절 (값이 클수록 빠름)
                this.rotation = window.bgmRotation;
            }
            requestAnimationFrame(animate);
        };
        requestAnimationFrame(animate);
    }
});
