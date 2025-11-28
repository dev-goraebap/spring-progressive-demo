package gg.jte.generated.ondemand.fragments;
@SuppressWarnings("unchecked")
public final class JteloadMoreButtonGenerated {
	public static final String JTE_NAME = "fragments/loadMoreButton.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,5,5,5,5,5,5,5,5,5,5,6,6,10,10,10,10,10,10,10,10,17,17,19,19,19,0,1,2,2,2,2};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, boolean hasMore, int nextPage, String orderType) {
		jteOutput.writeContent("\r\n<div id=\"load-more-container\"\r\n    ");
		var __jte_html_attribute_0 = hasMore ? "w-full flex justify-center p-4" : "hidden";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("div", "class");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("div", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n    ");
		if (hasMore) {
			jteOutput.writeContent("\r\n        <button class=\"btn\"\r\n                hx-get=\"/\"\r\n                hx-boost=\"false\"\r\n                hx-vals='{ \"page\": ");
			jteOutput.setContext("button", "hx-vals");
			jteOutput.writeUserContent(nextPage);
			jteOutput.setContext("button", null);
			jteOutput.writeContent(", \"orderType\": \"");
			jteOutput.setContext("button", "hx-vals");
			jteOutput.writeUserContent(orderType != null ? orderType : "");
			jteOutput.setContext("button", null);
			jteOutput.writeContent("\" }'\r\n                hx-target=\"#post-list\"\r\n                hx-swap=\"beforeend transition:false\"\r\n                hx-indicator=\"#load-more-spinner\">\r\n            <span id=\"load-more-spinner\" class=\"loading loading-spinner htmx-indicator\"></span>\r\n            더보기\r\n        </button>\r\n    ");
		}
		jteOutput.writeContent("\r\n</div>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		boolean hasMore = (boolean)params.get("hasMore");
		int nextPage = (int)params.get("nextPage");
		String orderType = (String)params.get("orderType");
		render(jteOutput, jteHtmlInterceptor, hasMore, nextPage, orderType);
	}
}
