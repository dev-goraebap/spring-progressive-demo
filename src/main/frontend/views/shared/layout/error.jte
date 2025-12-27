@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String title = "Error"
@param gg.jte.Content content

<!DOCTYPE html>
<html lang="ko" data-theme="${getTheme()}" class="overflow-y-scroll">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>데브고래밥 | ${title}</title>
    <link rel="icon" type="image/png" href="/images/logo.png">

    @if(getTheme() == null)
        <script>
            document.documentElement.setAttribute('data-theme',
                window.matchMedia('(prefers-color-scheme: light)').matches ? 'cupcake' : 'dark');
        </script>
    @endif

    <link rel="stylesheet" href="/css/font.css">

    @if(isDev())
    <link rel="stylesheet" href="http://localhost:5173/src/app/style.css">
    <script type="module" src="http://localhost:5173/@vite/client"></script>
    @else
    <link rel="stylesheet" href="/${viteCss()}">
    @endif
</head>
<body class="min-h-screen flex items-center justify-center">
    ${content}
</body>
</html>
