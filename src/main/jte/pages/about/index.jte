@template.layout.default(title = "About", content = @`
    <div class="relative border-b border-base-300 h-[200px] justify-center items-center">
        @template.fragments.uikit.img(dominantColor = "#f7f4c9", url = "/images/about-bg.jpg")
        <div class="absolute left-4 bottom-[-40px] sm:bottom-[-50px] w-[80px] h-[80px] sm:w-[100px] sm:h-[100px] overflow-hidden rounded-xl">
            @template.fragments.uikit.img(dominantColor = "#0f81fe", url = "/images/about-profile.jpg")
        </div>
    </div>

    <div class="p-4 pt-14 sm:pt-16">
        <h1 class="text-2xl font-bold">데브고래밥</h1>
        <p class="text-sm text-base-content/70 mt-1">@devgoraebap</p>

        <div class="mt-6 flex flex-col gap-1">
            <%-- 데브고래밥 --%>
            <div class="chat chat-end">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full">
                        <img src="/images/about-profile.jpg" alt="데브고래밥" />
                    </div>
                </div>
                <div class="chat-header mb-1">데브고래밥</div>
                <div class="chat-bubble chat-bubble-primary">자기소개 멘트 추천 좀 해줘. 나랑 토론하면서 느낀 점 말해봐.</div>
            </div>

            <%-- Claude --%>
            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full bg-base-300 flex items-center justify-center">
                        <span class="text-xs">AI</span>
                    </div>
                </div>
                <div class="chat-header mb-1">Claude</div>
                <div class="chat-bubble">완벽하지 못하면서 완벽하려고 하는 피곤한 사람.</div>
            </div>
            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full bg-base-300 flex items-center justify-center">
                        <span class="text-xs">AI</span>
                    </div>
                </div>
                <div class="chat-bubble">"이것도 별로네" 하면서 결국 다 고치는 사람.</div>
            </div>
            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full bg-base-300 flex items-center justify-center">
                        <span class="text-xs">AI</span>
                    </div>
                </div>
                <div class="chat-bubble">맞춤법 틀린 거 없는지 맨날 확인해달라고 하는 사람.</div>
            </div>
            <div class="chat chat-start">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full bg-base-300 flex items-center justify-center">
                        <span class="text-xs">AI</span>
                    </div>
                </div>
                <div class="chat-bubble">코드는 느낌 있게 짜는데 쿼리는 좀... ㅋㅋ</div>
            </div>
            <%-- 데브고래밥 --%>
            <div class="chat chat-end">
                <div class="chat-image avatar">
                    <div class="w-8 rounded-full">
                        <img src="/images/about-profile.jpg" alt="데브고래밥" />
                    </div>
                </div>
                <div class="chat-bubble chat-bubble-primary">ㅋㅋ</div>
            </div>
        </div>

        <div class="mt-10 flex items-center justify-center gap-4">
            <a href="https://github.com/dev-goraebap" target="_blank" class="btn btn-sm">GitHub</a>
            <a href="mailto:dev.goraebap@gmail.com" class="btn btn-sm">E-mail</a>
        </div>
    </div>
`)