package ch.cloudcoins.migration.boundary;

import ch.cloudcoins.environment.control.EnvironmentService;
import liquibase.exception.LiquibaseException;
import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.SQLException;

import static ch.cloudcoins.environment.EnvironmentVariables.DB_INSERT_TEST_DATA;
import static ch.cloudcoins.environment.EnvironmentVariables.DB_RESET_DATA;

@Dependent
public class LiquibaseProducer {
    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseProducer.class);

    @Inject
    private EnvironmentService environmentService;

    /**
     * Read the Datasource from InitialContext instead of resource lookup.
     */
    private DataSource getDataSource() throws NamingException {
        DataSource ds;
        try {
            Context initCtx = new InitialContext();
            ds = (DataSource) initCtx.lookup("java:jboss/datasources/cloudcoinsLiquibaseDS");
        } catch (NamingException e) {
            LOG.error("liquibaseDatasource was not configured under java:jboss/datasources/cloudcoinsLiquibaseDS");
            throw e;
        }
        return ds;
    }

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();

        boolean withTestData = Boolean.valueOf(environmentService.getValue(DB_INSERT_TEST_DATA));
        if (withTestData) {
            config.setChangeLog("liquibase/auto.db.changelog.with.testdata.xml");
        } else {
            config.setChangeLog("liquibase/auto.db.changelog.xml");
        }

        config.setDropFirst(Boolean.valueOf(environmentService.getValue(DB_RESET_DATA)));
        return config;
    }

    @Produces
    @LiquibaseType
    public DataSource createDataSource() throws SQLException, NamingException, LiquibaseException {
        return getDataSource();
    }

    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }
}
