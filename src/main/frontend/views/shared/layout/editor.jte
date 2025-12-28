@import static xyz.goraebap.blog.shared.jte.JteContext.*

@param String title = "에디터"
@param gg.jte.Content content

!{var siteName = "데브고래밥 스튜디오";}

<!DOCTYPE html>
<html lang="ko" data-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>${siteName} | ${title}</title>
    <link rel="icon" type="image/png" href="/images/logo.png">

    @template.shared.layout.common.fouc()
    @template.shared.layout.common.headAssets(entry = "app.editor")
</head>
<body class="bg-base-100">
    <div class="container max-w-6xl mx-auto">
        ${content}
    </div>
</body>
</html>
