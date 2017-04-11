package net.iryndin.mtapi;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.core.TransactionEntity;
import net.iryndin.mtapi.db.AccountDao;
import net.iryndin.mtapi.db.TransactionDao;
import net.iryndin.mtapi.resources.AccountResource;
import net.iryndin.mtapi.resources.TransactionResource;

import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author iryndin
 * @since 10/04/17
 */
public class MTAPIApplication extends Application<MTAPIConfiguration> {

    public static void main(String[] args) throws Exception {
            new MTAPIApplication().run(args);
        }

        private final HibernateBundle<MTAPIConfiguration> hibernateBundle =
                new HibernateBundle<MTAPIConfiguration>(AccountEntity.class, TransactionEntity.class) {
                    @Override
                    public DataSourceFactory getDataSourceFactory(MTAPIConfiguration configuration) {
                        return configuration.getDataSourceFactory();
                    }
                };

        @Override
        public String getName() {
            return "Money Transfer API";
        }

        @Override
        public void initialize(Bootstrap<MTAPIConfiguration> bootstrap) {
            bootstrap.addBundle(hibernateBundle);
        }

        @Override
        public void run(MTAPIConfiguration configuration, Environment environment) {
            final AccountDao accountDao = new AccountDao(hibernateBundle.getSessionFactory());
            final TransactionDao transactionDao = new TransactionDao(hibernateBundle.getSessionFactory());
            final ExceptionMapper exMapper = new GeneralExceptionMapper();

            environment.jersey().register(exMapper);
            environment.jersey().register(new AccountResource(accountDao));
            environment.jersey().register(new TransactionResource(accountDao, transactionDao));
        }
}
