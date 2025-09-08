package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/my_first_project_db?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Драйвер не обнаружен", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    private static final SessionFactory sessionFactory = new Configuration()
            .setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
            .setProperty("hibernate.connection.url", URL)
            .setProperty("hibernate.connection.username", USERNAME)
            .setProperty("hibernate.connection.password", PASSWORD)
            .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
            .setProperty("hibernate.show_sql", "true")
            .setProperty("hibernate.current_session_context_class", "thread")
            .setProperty("hibernate.hbm2ddl.auto", "update")
            .addAnnotatedClass(User.class)
            .buildSessionFactory();
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}




