package gg.jte.generated.ondemand.fragments;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
@SuppressWarnings("unchecked")
public final class JtepostItemGenerated {
	public static final String JTE_NAME = "fragments/postItem.jte";
	public static final int[] JTE_LINE_INFO = {0,0,2,2,2,2,5,5,5,9,9,9,9,11,11,11,11,11,11,11,11,11,12,12,12,12,18,18,19,19,19,20,20,20,23,23,23,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, PostViewModel post) {
		jteOutput.writeContent("\r\n<li class=\"flex flex-col gap-3 items-start p-4 border-b-4 border-base-300\">\r\n    ");
		if (post.getThumbnailUrl() != null) {
			jteOutput.writeContent("\r\n        <div x-data=\"{ loaded: false }\"\r\n             x-init=\"$nextTick(() => { if ($refs.img.complete) loaded = true })\"\r\n             class=\"w-full h-[170px] flex items-center justify-center overflow-hidden transition-colors duration-700 rounded-md\"\r\n             style=\"background-color: ");
			jteOutput.setContext("div", "style");
			jteOutput.writeUserContent(post.getThumbnailDominantColor() != null ? post.getThumbnailDominantColor() : "#e5e7eb");
			jteOutput.setContext("div", null);
			jteOutput.writeContent("\">\r\n            <img x-ref=\"img\"\r\n                ");
			var __jte_html_attribute_0 = post.getThumbnailUrl();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
				jteOutput.writeContent(" src=\"");
				jteOutput.setContext("img", "src");
				jteOutput.writeUserContent(__jte_html_attribute_0);
				jteOutput.setContext("img", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent("\r\n                 alt=\"");
			jteOutput.setContext("img", "alt");
			jteOutput.writeUserContent(post.getTitle());
			jteOutput.setContext("img", null);
			jteOutput.writeContent(" thumbnail image\"\r\n                 loading=\"lazy\"\r\n                 @load=\"loaded = true\"\r\n                 :class=\"loaded ? 'opacity-100' : 'opacity-0'\"\r\n                 class=\"w-full h-full object-cover transition-opacity duration-500\">\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    <h2 class=\"text-xl font-bold\">");
		jteOutput.setContext("h2", null);
		jteOutput.writeUserContent(post.getTitle());
		jteOutput.writeContent("</h2>\r\n    <p class=\"text-sm\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(post.getSummary());
		jteOutput.writeContent("</p>\r\n    <button class=\"font-bold\">Read more</button>\r\n</li>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		PostViewModel post = (PostViewModel)params.get("post");
		render(jteOutput, jteHtmlInterceptor, post);
	}
}
