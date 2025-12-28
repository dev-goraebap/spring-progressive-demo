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

    @template.shared.layout.common.fouc()
    @template.shared.layout.common.headAssets()
</head>
<body class="min-h-screen flex items-center justify-center">
    ${content}
</body>
</html>
