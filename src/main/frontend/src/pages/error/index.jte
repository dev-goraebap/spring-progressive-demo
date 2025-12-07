@param int status
@param String error
@param String message

@template.shared.layout.error(title = error, content = @`
    <div class="flex flex-col items-center justify-center p-8 text-center">
        <div class="text-[120px] sm:text-[180px] font-bold text-base-content/20 leading-none">${status}</div>
        <h1 class="text-3xl sm:text-5xl font-bold -mt-4">${error}</h1>

        <div class="mt-16">
            <p class="text-base-content/70 mb-6">${message}</p>
            <a href="/" class="btn btn-primary">
                홈으로 돌아가기
            </a>
        </div>
    </div>
`)
