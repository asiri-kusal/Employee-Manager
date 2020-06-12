/**
 * The DataSourceConfiguration class for manage data source and transactional.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import java.util.Properties;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:ecrm-${spring.profiles.active}-db.properties"})
public class DataSourceConfiguration {

        @Autowired
        private Environment env;

        @Bean(name = "ccbs-datasource")
        @Primary
        public DataSource ccbsDataSource() {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(env.getProperty("ccbs.jdbc.driverClassName"));
            dataSource.setJdbcUrl(env.getProperty("ccbs.jdbc.url"));
            dataSource.setUsername(env.getProperty("ccbs.jdbc.user"));
            dataSource.setPassword(env.getProperty("ccbs.jdbc.pass"));
            dataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("ccbs.jdbc.connectionPool")));
            dataSource.setMaxLifetime(Long.parseLong(env.getProperty("ccbs.jdbc.maxLifetime")));
            dataSource.setMinimumIdle(Integer.parseInt(env.getProperty("ccbs.jdbc.minIdle")));
            return dataSource;
        }


        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            final LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
            emf.setDataSource(ccbsDataSource());
            emf.setPackagesToScan("lk.dialog.ms");

            final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            emf.setJpaVendorAdapter(vendorAdapter);
            emf.setJpaProperties(hibernateProperties());

            return emf;
        }

        private Properties hibernateProperties() {
            final Properties hibernateProperties = new Properties();
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
            hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
            hibernateProperties.setProperty("hibernate.show_sql", "true");

            return hibernateProperties;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            final JpaTransactionManager transactionManager = new JpaTransactionManager();
            transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
            return transactionManager;
        }
}