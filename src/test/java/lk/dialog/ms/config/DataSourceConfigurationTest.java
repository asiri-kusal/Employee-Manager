/**
 * The DataSourceConfiguration Test.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import lk.dialog.ms.BaseTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class DataSourceConfigurationTest extends BaseTestUtil {

    private DataSourceConfiguration dataSourceConfiguration;

    @Before
    public void init() throws NoSuchFieldException, IllegalAccessException {
        dataSourceConfiguration = new DataSourceConfiguration();
        mockEnvironment();
    }

    @Test
    public void testCcbsDataSource(){
        DataSource dataSource = dataSourceConfiguration.ccbsDataSource();
        assertNotNull("DataSource object should not be null", dataSource);
    }

    @Test
    public void testEntityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = dataSourceConfiguration.entityManagerFactory();
        assertNotNull("LocalContainerEntityManagerFactoryBean object should not be null", entityManagerFactory);
    }

    @Test
    public void testTransactionManager(){
        PlatformTransactionManager transactionManager = dataSourceConfiguration.transactionManager();
        assertNotNull("PlatformTransactionManager object should not be null", transactionManager);
    }

    private void mockEnvironment() throws NoSuchFieldException, IllegalAccessException {
        Environment environment = mock(Environment.class);
        when(environment.getProperty("ccbs.jdbc.driverClassName")).thenReturn("com.mysql.cj.jdbc.Driver");
        when(environment.getProperty("ccbs.jdbc.url")).thenReturn("jdbc:mysql://localhost:3306/testdb");
        when(environment.getProperty("ccbs.jdbc.user")).thenReturn("root");
        when(environment.getProperty("ccbs.jdbc.pass")).thenReturn("");
        when(environment.getProperty("ccbs.jdbc.connectionPool")).thenReturn("100");
        when(environment.getProperty("ccbs.jdbc.maxLifetime")).thenReturn("540000");
        when(environment.getProperty("ccbs.jdbc.minIdle")).thenReturn("2");
        when(environment.getProperty("hibernate.hbm2ddl.auto")).thenReturn("update");
        when(environment.getProperty("hibernate.dialect")).thenReturn("org.hibernate.dialect.MySQL5InnoDBDialect");
        setValueToPrivateField(dataSourceConfiguration, "env", environment);
    }

}