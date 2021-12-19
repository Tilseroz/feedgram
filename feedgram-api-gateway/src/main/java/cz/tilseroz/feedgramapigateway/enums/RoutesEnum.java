package cz.tilseroz.feedgramapigateway.enums;

public enum RoutesEnum {

    FEED_SERVICE("feed-service", "/feed/**", "lb://feed-service"),
    AUTH_SERVICE("auth-service", "/auth/**", "lb://auth-service"),
    GRAPH_SERVICE("graph-service", "/graph/**", "lb://graph-service"),
    POST_SERVICE("post-service", "/post/**", "lb://post-service");

    private final String serviceId;
    private final String pathPattern;
    private final String uri;

    RoutesEnum(String serviceId, String pathPattern, String uri) {
        this.serviceId = serviceId;
        this.pathPattern = pathPattern;
        this.uri = uri;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public String getUri() {
        return uri;
    }
}
