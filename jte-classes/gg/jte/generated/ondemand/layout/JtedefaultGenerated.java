package gg.jte.generated.ondemand.layout;
@SuppressWarnings("unchecked")
public final class JtedefaultGenerated {
	public static final String JTE_NAME = "layout/default.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,12,12,12,12,14,17,17,18,22,22,23,24,24,24,24,25,25,25,25,26,26,30,30,33,33,33,36,36,39,39,39,0,1,2,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String title, boolean isDev, String viteCss, String viteJs, gg.jte.Content content) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ko\" data-theme=\"cupcake\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <meta name=\"htmx-config\" content='{\"globalViewTransitions\": true}'>\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(title);
		jteOutput.writeContent("</title>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <link rel=\"stylesheet\" href=\"/css/font.css\">\r\n\r\n    ");
		if (isDev) {
			jteOutput.writeContent("\r\n        ");
			jteOutput.writeContent("\r\n        <link rel=\"stylesheet\" href=\"http://localhost:5173/src/style.css\">\r\n        <script type=\"module\" src=\"http://localhost:5173/@vite/client\"></script>\r\n        <script type=\"module\" src=\"http://localhost:5173/src/app.js\"></script>\r\n    ");
		} else {
			jteOutput.writeContent("\r\n        ");
			jteOutput.writeContent("\r\n        <link rel=\"stylesheet\" href=\"/");
			jteOutput.setContext("link", "href");
			jteOutput.writeUserContent(viteCss);
			jteOutput.setContext("link", null);
			jteOutput.writeContent("\">\r\n        <script type=\"module\" src=\"/");
			jteOutput.setContext("script", "src");
			jteOutput.writeUserContent(viteJs);
			jteOutput.setContext("script", null);
			jteOutput.writeContent("\"></script>\r\n    ");
		}
		jteOutput.writeContent("\r\n</head>\r\n<body hx-boost=\"true\">\r\n<div id=\"htmx-progress\"></div>\r\n");
		gg.jte.generated.ondemand.fragments.JteheaderGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n\r\n<main>\r\n    ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n</main>\r\n\r\n");
		gg.jte.generated.ondemand.fragments.JtefooterGenerated.render(jteOutput, jteHtmlInterceptor);
		jteOutput.writeContent("\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String title = (String)params.getOrDefault("title", "Demo");
		boolean isDev = (boolean)params.getOrDefault("isDev", false);
		String viteCss = (String)params.getOrDefault("viteCss", "");
		String viteJs = (String)params.getOrDefault("viteJs", "");
		gg.jte.Content content = (gg.jte.Content)params.get("content");
		render(jteOutput, jteHtmlInterceptor, title, isDev, viteCss, viteJs, content);
	}
}
