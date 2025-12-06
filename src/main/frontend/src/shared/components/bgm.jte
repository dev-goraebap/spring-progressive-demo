<%-- BGM Player Component --%>
<div x-data>
    <div class="relative w-full pt-[70px]">
        <%-- 바이닐 디스크 --%>
        <div class="absolute top-0 left-1/2 -translate-x-1/2 w-[160px] h-[160px] z-10">
            <div class="w-full h-full rounded-full overflow-hidden shadow-xl animate-[spin_4s_linear_infinite]"
                 :style="$store.bgm.isPlaying ? 'animation-play-state: running' : 'animation-play-state: paused'">
                <img x-bind:src="$store.bgm.track?.cover || '/bgm/jane-doe.webp'" alt="Album Art"
                     class="w-full h-full object-cover"/>
                <%-- 중앙 원 --%>
                <div class="absolute inset-0 flex justify-center items-center">
                    <div class="rounded-full w-10 h-10 bg-base-300 border-4 border-base-100"></div>
                </div>
            </div>
        </div>

        <%-- 컨트롤 패널 --%>
        <div class="relative bg-base-200 border border-base-300 rounded-xl pt-24 pb-4 px-4">
            <%-- 곡 제목 --%>
            <p class="text-center text-sm font-medium mb-4 truncate"
               x-text="$store.bgm.track?.title || 'No Track'"></p>

            <%-- 프로그레스 바 --%>
            <div class="flex items-center gap-2 mb-4">
                <span class="text-xs text-base-content/70" x-text="$store.bgm.currentTime">0:00</span>
                <input type="range" min="0" max="100"
                       x-bind:value="$store.bgm.progress"
                       @input="$store.bgm.seek($event.target.value)"
                       class="range range-xs range-primary flex-1"/>
                <span class="text-xs text-base-content/70" x-text="$store.bgm.duration">0:00</span>
            </div>

            <%-- 컨트롤 버튼 --%>
            <div class="flex justify-center items-center gap-4">
                <button @click="$store.bgm.prev()" class="btn btn-ghost btn-sm btn-circle">
                    <div class="w-5">@template.shared.svg.prev()</div>
                </button>
                <button @click="$store.bgm.toggle()" class="btn btn-primary btn-circle">
                    <div class="w-5" x-show="!$store.bgm.isPlaying">@template.shared.svg.play()</div>
                    <div class="w-5" x-show="$store.bgm.isPlaying" x-cloak>@template.shared.svg.pause()</div>
                </button>
                <button @click="$store.bgm.next()" class="btn btn-ghost btn-sm btn-circle">
                    <div class="w-5">@template.shared.svg.next()</div>
                </button>
            </div>

            <%-- 볼륨 --%>
            <div class="flex items-center gap-2 mt-4">
                <div class="w-4">@template.shared.svg.volume()</div>
                <input type="range" min="0" max="100"
                       :value="$store.bgm.volume"
                       @input="$store.bgm.setVolume($event.target.value)"
                       class="range range-xs flex-1"/>
            </div>
        </div>
    </div>
</div>
