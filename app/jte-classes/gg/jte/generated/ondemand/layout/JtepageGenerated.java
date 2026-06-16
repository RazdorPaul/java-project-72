package gg.jte.generated.ondemand.layout;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtepageGenerated {
	public static final String JTE_NAME = "layout/page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,3,3,3,3,12,12,12,12,44,44,47,47,47,52,52,53,53,53,63,63,63,3,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String title, Content content, String flash) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html lang=\"ru\">\r\n    <head>\r\n        <meta charset=\"UTF-8\">\r\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n        <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(title);
		jteOutput.writeContent("</title>\r\n        <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css\"\r\n              rel=\"stylesheet\"\r\n              integrity=\"sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB\"\r\n              crossorigin=\"anonymous\">\r\n    </head>\r\n    <body class=\"d-flex flex-column min-vh-100\">\r\n        <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\r\n            <div class=\"container-fluid\">\r\n                <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\r\n                <button class=\"navbar-toggler\"\r\n                        type=\"button\"\r\n                        data-bs-toggle=\"collapse\"\r\n                        data-bs-target=\"#navbarNav\"\r\n                        aria-controls=\"navbarNav\"\r\n                        aria-expanded=\"false\"\r\n                        aria-label=\"Toggle navigation\">\r\n                    <span class=\"navbar-toggler-icon\"></span>\r\n                </button>\r\n                <div class=\"collapse navbar-collapse\"\r\n                     id=\"navbarNav\">\r\n                    <ul class=\"navbar-nav\">\r\n                        <li class=\"nav-item\">\r\n                            <a class=\"nav-link active\"\r\n                               aria-current=\"page\"\r\n                               href=\"/\">Сайты</a>\r\n                        </li>\r\n                    </ul>\r\n                </div>\r\n            </div>\r\n        </nav>\r\n        <main class=\"container mt-4 flex-grow-1\">\r\n            ");
		if (flash != null && !flash.isBlank()) {
			jteOutput.writeContent("\r\n                <div class=\"alert alert-info alert-dismissible fade show\"\r\n                     role=\"alert\">\r\n                    ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(flash);
			jteOutput.writeContent("\r\n                    <button type=\"button\"\r\n                            class=\"btn-close\"\r\n                            data-bs-dismiss=\"alert\"></button>\r\n                </div>\r\n            ");
		}
		jteOutput.writeContent("\r\n            ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n        </main>\r\n        <footer class=\"bg-light border-top py-4 mt-auto\">\r\n            <div class=\"container text-center\">\r\n                <a href=\"https://hexlet.io\"\r\n                   class=\"text-decoration-none\">Hexlet</a>\r\n            </div>\r\n        </footer>\r\n    </body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String title = (String)params.get("title");
		Content content = (Content)params.get("content");
		String flash = (String)params.get("flash");
		render(jteOutput, jteHtmlInterceptor, title, content, flash);
	}
}
