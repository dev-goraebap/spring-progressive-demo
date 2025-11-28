package gg.jte.generated.ondemand.pages;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "pages/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,11,11,11,11,14,14,14,14,16,16,16,16,20,20,21,21,22,22,25,25,26,26,26,27,27,27,3,4,5,6,7,8,9,9,9,9};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<PostViewModel> posts, boolean hasMore, int nextPage, String orderType, boolean isDev, String viteCss, String viteJs) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtedefaultGenerated.render(jteOutput, jteHtmlInterceptor, "Home", isDev, viteCss, viteJs, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n    <div class=\"flex gap-2 p-4\">\r\n        <a href=\"/\"\r\n           class=\"btn btn-sm ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent(orderType == null || orderType.isEmpty() ? "btn-active" : "");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">최근</a>\r\n        <a href=\"/?orderType=POPULAR\"\r\n           class=\"btn btn-sm ");
				jteOutput.setContext("a", "class");
				jteOutput.writeUserContent("POPULAR".equals(orderType) ? "btn-active" : "");
				jteOutput.setContext("a", null);
				jteOutput.writeContent("\">인기</a>\r\n    </div>\r\n\r\n    <ul id=\"post-list\" class=\"flex flex-col\">\r\n        ");
				for (var post : posts) {
					jteOutput.writeContent("\r\n            ");
					gg.jte.generated.ondemand.fragments.JtepostItemGenerated.render(jteOutput, jteHtmlInterceptor, post);
					jteOutput.writeContent("\r\n        ");
				}
				jteOutput.writeContent("\r\n    </ul>\r\n\r\n    ");
				gg.jte.generated.ondemand.fragments.JteloadMoreButtonGenerated.render(jteOutput, jteHtmlInterceptor, hasMore, nextPage, orderType);
				jteOutput.writeContent("\r\n");
			}
		});
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<PostViewModel> posts = (List<PostViewModel>)params.get("posts");
		boolean hasMore = (boolean)params.get("hasMore");
		int nextPage = (int)params.get("nextPage");
		String orderType = (String)params.get("orderType");
		boolean isDev = (boolean)params.getOrDefault("isDev", false);
		String viteCss = (String)params.getOrDefault("viteCss", "");
		String viteJs = (String)params.getOrDefault("viteJs", "");
		render(jteOutput, jteHtmlInterceptor, posts, hasMore, nextPage, orderType, isDev, viteCss, viteJs);
	}
}
