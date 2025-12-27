<%-- BGM Player Component - 글래스모피즘 카드 --%>
<div x-data="{ imgLoaded: false }">
    <div class="relative rounded-2xl overflow-hidden">
        <%-- 배경 블러 이미지 --%>
        <div class="absolute inset-0">
            <img x-bind:src="$store.bgm.track?.cover || '/bgm/jane-doe.webp'"
                 @load="imgLoaded = true"
                 alt=""
                 class="w-full h-full object-cover scale-110 blur-xl opacity-80"/>
            <%-- 어두운 오버레이 (라이트모드 대응) --%>
            <div class="absolute inset-0 bg-black/20"></div>
        </div>

        <%-- 글래스 오버레이 --%>
        <div class="relative backdrop-blur-sm bg-black/20 p-4">
            <%-- 상단: 앨범아트 + 곡 정보 --%>
            <div class="flex items-center gap-4">
                <%-- 앨범아트 (원형) --%>
                <div class="w-16 h-16 rounded-full overflow-hidden shrink-0 shadow-lg ring-2 ring-white/20"
                     :style="'transform: rotate(' + $store.bgm.rotation + 'deg)'">
                    <img x-bind:src="$store.bgm.track?.cover || '/bgm/jane-doe.webp'"
                         alt="Album Art"
                         class="w-full h-full object-cover"/>
                </div>

                <%-- 곡 정보 --%>
                <div class="flex-1 min-w-0">
                    <p class="text-base font-semibold truncate text-white drop-shadow-md"
                       x-text="$store.bgm.track?.title || 'No Track'"
                       :title="$store.bgm.track?.title || 'No Track'"></p>
                    <p class="text-sm text-white/70 truncate"
                       x-text="$store.bgm.track?.artist || 'Unknown'"
                       :title="$store.bgm.track?.artist || 'Unknown'"></p>
                </div>
            </div>

            <%-- 프로그레스 바 --%>
            <div class="flex items-center gap-2 mt-4">
                <span class="text-xs text-white/60 w-8 text-right" x-text="$store.bgm.currentTime">0:00</span>
                <input type="range" min="0" max="100"
                       x-bind:value="$store.bgm.progress"
                       @input="$store.bgm.seek($event.target.value)"
                       class="range range-xs range-primary flex-1"/>
                <span class="text-xs text-white/60 w-8" x-text="$store.bgm.duration">0:00</span>
            </div>

            <%-- 컨트롤 버튼 + 볼륨 --%>
            <div class="flex justify-center items-center gap-6 mt-4">
                <button @click="$store.bgm.prev()" class="btn btn-ghost btn-sm btn-circle text-white hover:bg-white/20">
                    <div class="w-4">@template.shared.svg.prev()</div>
                </button>
                <button @click="$store.bgm.toggle()" class="btn btn-circle bg-white hover:bg-white/80 border-0 shadow-lg">
                    <div class="w-5 text-neutral" x-show="!$store.bgm.isPlaying">@template.shared.svg.play()</div>
                    <div class="w-5 text-neutral" x-show="$store.bgm.isPlaying" x-cloak>@template.shared.svg.pause()</div>
                </button>
                <button @click="$store.bgm.next()" class="btn btn-ghost btn-sm btn-circle text-white hover:bg-white/20">
                    <div class="w-4">@template.shared.svg.next()</div>
                </button>

                <%-- 음소거 버튼 (우측 하단) --%>
                <button @click="$store.bgm.toggleMute()"
                        class="absolute bottom-3 right-3 btn btn-ghost btn-xs btn-circle text-white/50 hover:text-white hover:bg-white/20">
                    <div class="w-4" x-show="!$store.bgm.isMuted">@template.shared.svg.volume()</div>
                    <div class="w-4" x-show="$store.bgm.isMuted" x-cloak>@template.shared.svg.volumeMute()</div>
                </button>
            </div>
        </div>
    </div>
</div>
