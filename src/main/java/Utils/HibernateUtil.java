package Utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Singleton instance of SessionFactory
    private static SessionFactory sessionFactory;

    // Private constructor to prevent instantiation
    private HibernateUtil() {}

    // Public method to get the SessionFactory
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Load configuration from hibernate.cfg.xml
                Configuration configuration = new Configuration().configure();

                // Optionally add annotated classes manually (if not using <mapping> in cfg.xml)
                //configuration.addAnnotatedClass(com.abhi.model.User.class);

                sessionFactory = configuration.buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    // Optional method to close the SessionFactory during shutdown
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
