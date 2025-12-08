@param boolean totpConfigured = true
@param String error = null

@template.shared.layout.blank(
    title = "로그인",
    content = @`
    <div class="min-h-screen flex items-center justify-center p-4">
        <div class="card w-full max-w-sm bg-base-100 border border-base-300">
            <div class="card-body p-8">
                <%-- 로고 영역 --%>
                <div class="text-center mb-6">
                    <div class="w-20 h-20 mx-auto mb-4">
                        <img src="/images/logo.png" alt="로고" class="w-full h-full object-contain"/>
                    </div>
                    <h1 class="text-2xl font-bold">데브고래밥 스튜디오</h1>
                    <p class="text-sm text-base-content/60 mt-1">관리자 로그인</p>
                </div>

                @if(!totpConfigured)
                    <div class="alert alert-warning text-sm">
                        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-5 w-5" fill="none" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                        </svg>
                        <span>TOTP가 설정되지 않았습니다.</span>
                    </div>
                @else
                    @if(error != null)
                        <div class="alert alert-error text-sm mb-4">
                            <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-5 w-5" fill="none" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <span>${error}</span>
                        </div>
                    @endif

                    <form method="post" action="/admin/login" class="space-y-4">
                        <div class="form-control">
                            <label class="label">
                                <span class="label-text font-medium">인증 코드</span>
                            </label>
                            <input type="text"
                                   name="code"
                                   placeholder="000000"
                                   class="input input-bordered input-lg text-center text-3xl tracking-[0.5em] font-mono"
                                   maxlength="6"
                                   pattern="[0-9]{6}"
                                   inputmode="numeric"
                                   autocomplete="one-time-code"
                                   autofocus
                                   required />
                            <label class="label">
                                <span class="label-text-alt text-base-content/50">Google Authenticator 앱 확인</span>
                            </label>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">로그인</button>
                    </form>
                @endif

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
