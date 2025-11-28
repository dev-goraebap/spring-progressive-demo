package gg.jte.generated.ondemand.fragments;
import xyz.goraebap.spring_progressive_demo.infra.view_model.PostViewModel;
import java.util.List;
@SuppressWarnings("unchecked")
public final class JtepostListItemsGenerated {
	public static final String JTE_NAME = "fragments/postListItems.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,3,8,8,8,9,9,10,10,12,13,13,20,20,20,20,20,20,20,20,28,28,33,33,34,34,34,3,4,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, List<PostViewModel> posts, boolean hasMore, int nextPage, String orderType) {
		jteOutput.writeContent("\r\n");
		for (var post : posts) {
			jteOutput.writeContent("\r\n    ");
			gg.jte.generated.ondemand.fragments.JtepostItemGenerated.render(jteOutput, jteHtmlInterceptor, post);
			jteOutput.writeContent("\r\n");
		}
		jteOutput.writeContent("\r\n\r\n");
		jteOutput.writeContent("\r\n");
		if (hasMore) {
			jteOutput.writeContent("\r\n    <div id=\"load-more-container\"\r\n         hx-swap-oob=\"true\"\r\n         class=\"w-full flex justify-center p-4\">\r\n        <button class=\"btn\"\r\n                hx-get=\"/\"\r\n                hx-boost=\"false\"\r\n                hx-vals='{ \"page\": ");
			jteOutput.setContext("button", "hx-vals");
			jteOutput.writeUserContent(nextPage);
			jteOutput.setContext("button", null);
			jteOutput.writeContent(", \"orderType\": \"");
			jteOutput.setContext("button", "hx-vals");
			jteOutput.writeUserContent(orderType != null ? orderType : "");
			jteOutput.setContext("button", null);
			jteOutput.writeContent("\" }'\r\n                hx-target=\"#post-list\"\r\n                hx-swap=\"beforeend transition:false\"\r\n                hx-indicator=\"#load-more-spinner\">\r\n            <span id=\"load-more-spinner\" class=\"loading loading-spinner htmx-indicator\"></span>\r\n            더보기\r\n        </button>\r\n    </div>\r\n");
		} else {
			jteOutput.writeContent("\r\n    <div id=\"load-more-container\"\r\n         hx-swap-oob=\"true\"\r\n         class=\"hidden\">\r\n    </div>\r\n");
		}
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		List<PostViewModel> posts = (List<PostViewModel>)params.get("posts");
		boolean hasMore = (boolean)params.get("hasMore");
		int nextPage = (int)params.get("nextPage");
		String orderType = (String)params.get("orderType");
		render(jteOutput, jteHtmlInterceptor, posts, hasMore, nextPage, orderType);
	}
}
