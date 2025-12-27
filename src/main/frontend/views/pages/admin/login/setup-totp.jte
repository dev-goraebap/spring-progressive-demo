@param String qrCode
@param String error = null

@template.shared.layout.blank(
    title = "TOTP 설정",
    content = @`
    <div class="min-h-screen flex items-center justify-center p-4">
        <div class="card w-full max-w-sm bg-base-100 border border-base-300">
            <div class="card-body p-8">
                <%-- 헤더 --%>
                <div class="text-center mb-6">
                    <div class="w-20 h-20 mx-auto mb-4">
                        <img src="/images/logo.png" alt="로고" class="w-full h-full object-contain"/>
                    </div>
                    <h1 class="text-2xl font-bold">TOTP 설정</h1>
                    <p class="text-sm text-base-content/60 mt-1">Google Authenticator 등록</p>
                </div>

                @if(error != null)
                    <div class="alert alert-error text-sm mb-4">
                        <span>${error}</span>
                    </div>
                @endif

                <%-- QR 코드 --%>
                <div class="flex justify-center mb-6">
                    <div class="bg-white p-3 rounded-xl shadow-inner">
                        <img src="${qrCode}" alt="TOTP QR Code" class="w-44 h-44"/>
                    </div>
                </div>

                <%-- 안내 --%>
                <div class="bg-base-200 rounded-lg p-4 text-sm text-base-content/70 space-y-2">
                    <p class="flex items-start gap-2">
                        <span class="text-primary font-bold">1.</span>
                        <span>Google Authenticator 앱을 실행하세요</span>
                    </p>
                    <p class="flex items-start gap-2">
                        <span class="text-primary font-bold">2.</span>
                        <span>+ 버튼 → QR 코드 스캔</span>
                    </p>
                    <p class="flex items-start gap-2">
                        <span class="text-primary font-bold">3.</span>
                        <span>위 QR 코드를 스캔하세요</span>
                    </p>
                </div>

                <div class="mt-6">
                    <a href="/admin/login" class="btn btn-primary btn-block">로그인하러 가기</a>
                </div>

                <div class="divider my-4"></div>

                <div class="text-center">
                    <a href="/" class="link link-hover text-sm text-base-content/50">
                        ← 블로그로 돌아가기
                    </a>
                </div>
            </div>
        </div>
    </div>
`)
