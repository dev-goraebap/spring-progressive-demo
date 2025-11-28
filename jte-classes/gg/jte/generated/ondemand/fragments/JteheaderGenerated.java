package gg.jte.generated.ondemand.fragments;
@SuppressWarnings("unchecked")
public final class JteheaderGenerated {
	public static final String JTE_NAME = "fragments/header.jte";
	public static final int[] JTE_LINE_INFO = {6,6,6,6,6,6,6,6,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		jteOutput.writeContent("<header>\r\n    <nav>\r\n        <a href=\"/\">Home</a>\r\n        <a href=\"/about\">About</a>\r\n    </nav>\r\n</header>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
