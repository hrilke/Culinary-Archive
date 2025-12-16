package com.cookbook.culinary_archive.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Pagination pagination = new Pagination();

    @Data
    public static class Pagination {
        private int defaultPage = 0;
        private int defaultSize = 10;
        private int maxSize = 100;
        private String defaultSortDirection = "desc";
        private String defaultSortField = "createdAt";
    }
}
