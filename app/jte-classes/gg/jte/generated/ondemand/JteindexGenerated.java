package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,3,3,25,25,25,26,26,26,26,26,26};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor) {
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, "Анализатор страниц", new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    <div class=\"row\">\n        <div class=\"col-12 col-md-10 col-lg-8 mx-auto border rounded-3 bg-light p-5\">\n            <h1 class=\"display-3\">Анализатор страниц</h1>\n            <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\n            <form action=\"/urls\" method=\"post\" class=\"row\">\n                <div class=\"col-8\">\n                    <label for=\"url-name\" class=\"visually-hidden\">Url для проверки</label>\n                    <input\n                            id=\"url-name\"\n                            type=\"text\"\n                            name=\"url\"\n                            class=\"form-control form-control-lg\"\n                            placeholder=\"https://www.example.com\"\n                    >\n                </div>\n                <div class=\"col-2\">\n                    <input type=\"submit\" class=\"btn btn-primary btn-lg ms-3 px-5 text-uppercase mx-3\" value=\"Проверить\">\n                </div>\n            </form>\n        </div>\n    </div>\n");
			}
		}, null);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		render(jteOutput, jteHtmlInterceptor);
	}
}
