package io.github.shshdxk;

import io.github.shshdxk.liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        return runLiquibase(dataSource);
    }

    private SpringLiquibase runLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase() {};
        liquibase.setContexts(null);
        liquibase.setDefaultSchema(null);
        liquibase.setDropFirst(false);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setShouldRun(true);
        return liquibase;
    }

}
