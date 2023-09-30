package hexlet.code.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config")
public class Config {
    private String defaultPath;

    public String getDefaultPath() {
        return this.defaultPath;
    }


}
