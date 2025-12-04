import Alpine from 'alpinejs';

// BGM 플레이리스트
const playlist = [
    { title: 'Jane Doe', src: '/bgm/jane-doe.mp3', cover: '/bgm/jane-doe.webp' },
    { title: 'Nanase Dear', src: '/bgm/nanase-dear.mp3', cover: '/bgm/nanase-dear.png' }
];

// Audio 객체 전역 유지 (페이지 이동해도 끊기지 않게)
if (!window.bgmAudio) {
    window.bgmAudio = new Audio();
    window.bgmAudio.volume = 0.5;
}

// 시간 포맷
function formatTime(seconds) {
    if (isNaN(seconds)) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return `${mins}:${secs.toString().padStart(2, '0')}`;
}

// BGM Alpine Store 등록
Alpine.store('bgm', {
    isPlaying: false,
    currentIndex: 0,
    currentTime: '0:00',
    duration: '0:00',
    progress: 0,
    volume: 50,

    init() {
        const audio = window.bgmAudio;

        audio.addEventListener('play', () => {
            this.isPlaying = true;
        });

        audio.addEventListener('pause', () => {
            this.isPlaying = false;
        });

        audio.addEventListener('ended', () => {
            this.next();
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

    next() {
        let newIndex = this.currentIndex + 1;
        if (newIndex >= playlist.length) newIndex = 0;
        this.load(newIndex);
        if (this.isPlaying) window.bgmAudio.play();
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
    }
});
