package pay.token.jooq.config;


import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.Settings;
import org.jooq.conf.StatementType;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Jooq DataSource Configuration
 *
 * @author Jinhyang
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.PROXY, proxyTargetClass = true)
@AutoConfigureAfter(TransactionAutoConfiguration.class) // for JOOQ
public class JooqConfig {
    private final DataSource dataSource;

    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * JOOQ configuration
     *
     * @return {@link DefaultConfiguration}
     */
    public DefaultConfiguration configuration() {
        final DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(dataSourceConnectionProvider());
        jooqConfiguration.set(springTransactionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(new ExceptionTranslator()));
        jooqConfiguration.set(SQLDialect.MYSQL);
        jooqConfiguration.set(settings());

        return jooqConfiguration;
    }

    /**
     * Data Source Connection Provider for JOOQ
     *
     * @return {@link DataSourceConnectionProvider}
     */
    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider() {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    /**
     * JOOQ DSLContext
     *
     * @return {@link DSLContext}
     */
    @Bean
    public DSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }

    /**
     * Settings for JOOQ
     *
     * @return {@link Settings}
     */
    @Bean
    public Settings settings() {
        return new Settings().withRenderCatalog(true).withRenderSchema(true)
                // Default to AS_IS
                .withRenderKeywordStyle(RenderKeywordStyle.LOWER)
                // Default to PREPARED_STATEMENT
                .withStatementType(StatementType.STATIC_STATEMENT)
                // Pretty Logging
                .withRenderFormatted(true)
                // Execute Logging - Defaults to true
                .withExecuteLogging(true);
    }

    /**
     * Transaction Provider for JOOQ
     *
     * @return {@link SpringTransactionProvider}
     */
    @Bean
    public SpringTransactionProvider springTransactionProvider() {
        return new SpringTransactionProvider(transactionManager());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
