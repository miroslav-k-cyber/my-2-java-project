package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import javax.xml.namespace.QName;

import org.slf4j.LoggerFactory;

public class UserDaoJDBCImpl implements UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    
    public UserDaoJDBCImpl() {
    }
    
    @Override
    public void createUsersTable() { // 1 метод
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица создана или уже существует.");
        } catch (SQLException e) {
            logger.error("Ошибка при создании таблицы: {}", e.getMessage(), e);
            throw new RuntimeException("Произошла ошибка при создании таблицы", e);
        }
    }
    
    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Успешное удаление таблицы");
        } catch (SQLException e) {
            logger.error("При удалении таблицы произошла ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }
    
    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            logger.info("Сотрудник  с именем '{}' добавлен в базу", name);
        } catch (SQLException e) {
            logger.error("При добавлении '{}'пользователя {} произола ошибка:", name, e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении пользователя в БД", e);
        }
    }
    
    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            logger.info("Пользователь успешно удален");
        } catch (SQLException e) {
            logger.error("При удалении пользователя с id={} произошла ошибка: ", id, e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")
                );
                user.setId(resultSet.getLong("id"));
                users.add(user);
            }
            logger.info("Общее количество загруженных пользователей: {}", users.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
        return users;
        }
        return users;
    }
    
    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Таблица users успешно очищена");
        } catch (SQLException e) {
            logger.error("При очистке таблицы произошла ошибка: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}
