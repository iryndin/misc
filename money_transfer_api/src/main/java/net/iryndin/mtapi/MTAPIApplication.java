package net.iryndin.mtapi;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.iryndin.mtapi.core.AccountEntity;
import net.iryndin.mtapi.resources.AccountResource;
import net.iryndin.mtapi.resources.TransactionResource;

/**
 * @author iryndin
 * @since 10/04/17
 */
public class MTAPIApplication extends Application<MTAPIConfiguration> {

    public static void main(String[] args) throws Exception {
            new MTAPIApplication().run(args);
        }

        private final HibernateBundle<MTAPIConfiguration> hibernateBundle =
                new HibernateBundle<MTAPIConfiguration>(AccountEntity.class) {
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
            environment.jersey().register(new AccountResource());
            environment.jersey().register(new TransactionResource());
            /*
            environment.healthChecks().register("template", new TemplateHealthCheck(template));
            environment.admin().addTask(new EchoTask());
            environment.jersey().register(DateRequiredFeature.class);
            environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new ExampleAuthenticator())
                    .setAuthorizer(new ExampleAuthorizer())
                    .setRealm("SUPER SECRET STUFF")
                    .buildAuthFilter()));
            environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
            environment.jersey().register(RolesAllowedDynamicFeature.class);
            environment.jersey().register(new HelloWorldResource(template));
            environment.jersey().register(new ViewResource());
            environment.jersey().register(new ProtectedResource());
            environment.jersey().register(new PeopleResource(dao));
            environment.jersey().register(new PersonResource(dao));
            environment.jersey().register(new FilteredResource());*/
        }
}
