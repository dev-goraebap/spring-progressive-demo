@param String title
@param boolean isDev = false
@param String viteCss = ""
@param String viteJs = ""

@template.layout.default(title = "About", isDev = isDev, viteCss = viteCss, viteJs = viteJs, content = @`
    <h1 class="text-3xl font-bold">${title}</h1>
    <p class="text-xs">About Page</p>
`)
