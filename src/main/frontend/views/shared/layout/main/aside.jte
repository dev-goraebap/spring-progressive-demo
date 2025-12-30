<%-- Bio --%>
<div class="mb-4">
    <div class="flex items-center gap-3">
        <div class="w-14 h-14 rounded-xl bg-base-200 overflow-hidden shrink-0 cursor-pointer"
             x-data="easterEgg"
             @click="handleClick()">
            <img src="/images/logo.png" alt="프로필" class="w-full h-full object-cover"/>
        </div>
        <div>
            <h2 class="text-lg font-bold">데브고래밥</h2>
            <p class="text-xs text-base-content/60">@devgoraebap</p>
        </div>
    </div>
    <p class="mt-3 text-sm text-base-content/80 leading-relaxed">
        <span class="line-through text-base-content/40">스택오버플로우의 단골손님이였던</span>
        Claude를 채찍질하는 개발자
    </p>
    <div class="flex gap-2 mt-3">
        <a href="https://github.com/dev-goraebap" target="_blank"
           class="btn btn-ghost btn-sm btn-circle">
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 0C5.374 0 0 5.373 0 12c0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.839 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.322 3.301 1.23A11.509 11.509 0 0112 5.803c1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576C20.566 21.797 24 17.3 24 12c0-6.627-5.373-12-12-12z"/>
            </svg>
        </a>
        <a href="mailto:dev.goraebap@gmail.com"
           class="btn btn-ghost btn-sm btn-circle">
            <div class="w-5 h-5">@template.shared.svg.mail()</div>
        </a>
    </div>
</div>

<div class="border-t border-base-300 pt-4">
    <%-- Music Player UI --%>
    @template.shared.components.bgm()
</div>

<%-- PWA 앱 설치 유도 --%>
<div class="mt-4 block rounded-xl overflow-hidden cursor-pointer hover:scale-[1.02] transition-transform"
     x-show="!$store.pwa.isInstalled"
     @click="$store.pwa.install()">
    <div class="relative">
        <img src="/images/pwa-banner.jpg" alt="앱 설치" class="w-full h-24 object-cover"/>
        <div class="absolute inset-0 bg-gradient-to-r from-black/70 to-transparent"></div>
        <div class="absolute inset-0 p-4 flex flex-col justify-center">
            <p class="font-bold text-white">앱 설치하기</p>
            <p class="text-xs text-white/80">홈 화면에 추가하고 새 글 알림도 받아보세요</p>
        </div>
    </div>
</div>

<%-- 요즘 관심있는 --%>
<div class="mt-4">
    <p class="text-xs text-base-content/50 mb-2">요즘 관심있는</p>
    <div class="flex flex-col gap-2">
        <a href="https://htmx.org/" target="_blank" rel="noopener noreferrer"
           class="flex items-center gap-3 p-3 rounded-lg bg-base-200 hover:bg-base-300 transition-colors">
            <div class="w-8 h-8 rounded bg-[#3d72d7] flex items-center justify-center text-white text-xs font-bold">&lt;/&gt;</div>
            <div>
                <p class="text-sm font-medium">HTMX</p>
                <p class="text-xs text-base-content/60">HTML을 위한 하이퍼미디어</p>
            </div>
        </a>
        <a href="https://phaser.io/" target="_blank" rel="noopener noreferrer"
           class="flex items-center gap-3 p-3 rounded-lg bg-base-200 hover:bg-base-300 transition-colors">
            <div class="w-8 h-8 rounded bg-[#1d1d1d] flex items-center justify-center">
                <svg class="w-5 h-5 text-[#99e94a]" viewBox="0 0 24 24" fill="currentColor">
                    <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
                </svg>
            </div>
            <div>
                <p class="text-sm font-medium">Phaser</p>
                <p class="text-xs text-base-content/60">HTML5 게임 프레임워크</p>
            </div>
        </a>
    </div>
</div>
