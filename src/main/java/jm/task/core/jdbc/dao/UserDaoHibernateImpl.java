package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);
    
    public UserDaoHibernateImpl() {
    
    }
    
    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";
        
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            logger.info("Таблица создана или уже существует.");
        } catch (Exception e) {
            logger.error("Ошибка при создании таблицы: {}", e.getMessage(), e);
            throw new RuntimeException("Произошла ошибка при создании таблицы", e);
        }
    }
    
    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            logger.info("Успешное удаление таблицы");
        } catch (Exception e) {
            logger.error("При удалении таблицы произошла ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }
    
    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info("Пользователь с именем '{}' добавлен в базу", name);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении '{}' пользователя: {}", name, e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении пользователя в БД", e);
        }
    }
    
    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.info("Пользователь {} удален", id);
            } else {
                logger.warn("Пользователь с id={} не найден", id);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("При удалении пользователя с id={} произошла ошибка: {}", id, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            users = session.createQuery("FROM User", User.class).list();
            transaction.commit();
            logger.info("Общее количество загруженных пользователей: {}", users.size());
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
        return users;
    }
    
    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
            logger.info("Таблица очищена");
        } catch (Exception e) {
            logger.error("При очистке таблицы произошла ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}

