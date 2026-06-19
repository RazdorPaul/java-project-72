package hexlet.code.utils;

public final class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String urlsPath() {
        return rootPath() + "urls";
    }

    public static String urlPath(String id) {
        return urlsPath() + "/" + id;
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String indexPath() {
        return "index.jte";
    }

    public static String urlsIndexPath() {
        return "urls/index.jte";
    }

    public static String urlShowPath() {
        return "urls/show.jte";
    }
}
