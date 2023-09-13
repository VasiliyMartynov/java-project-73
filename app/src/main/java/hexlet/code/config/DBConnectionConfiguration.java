package hexlet.code.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(DBConfiguration.class)
public class DBConnectionConfiguration {

    private DBConfiguration dbConfiguration;
    public DBConnectionConfiguration(DBConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }
    @Profile("dev")
    @Bean
    public String devDBCcnnection() {
        System.out.println("DB Connection for Dev");
        System.out.println(dbConfiguration.getUrl());
        System.out.println(dbConfiguration.getUserName());
        System.out.println(dbConfiguration.getPassword());
        System.out.println(dbConfiguration.getDriverClassName());
        return "DB Connection for Dev";
    }

    @Profile("prod")
    @Bean
    public String devProdCcnnection() {
        System.out.println("DB Connection for Prod");
        System.out.println(dbConfiguration.getUrl());
        System.out.println(dbConfiguration.getUserName());
        System.out.println(dbConfiguration.getPassword());
        System.out.println(dbConfiguration.getDriverClassName());
        return "DB Connection for Prod";
    }
}
