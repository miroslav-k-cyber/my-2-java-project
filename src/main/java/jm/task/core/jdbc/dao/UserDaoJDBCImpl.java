package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;

import org.slf4j.Logger;

public class UserDaoJDBCImpl implements UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    
    public UserDaoJDBCImpl() {
    }
    
    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT)";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement pstm = connect.prepareStatement(sql)) {
                pstm.executeUpdate();
                connect.commit();
                logger.info("Таблица создана или уже существует.");
            } catch (SQLException e) {
                connect.rollback();
                logger.error("Ошибка при создании таблицы: {}", e.getMessage(), e);
                throw new RuntimeException("Произошла ошибка при создании таблицы", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }
    
    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement pstm = connect.prepareStatement(sql)) {
                pstm.executeUpdate();
                connect.commit();
                logger.info("Успешное удаление таблицы");
            } catch (SQLException e) {
                connect.rollback();
                logger.error("При удалении таблицы произошла ошибка: {}", e.getMessage(), e);
                throw new RuntimeException("Ошибка при удалении таблицы", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении таблицы", e);
        }
    }
    
    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                connect.commit();
                logger.info("Сотрудник с именем '{}' добавлен в базу", name);
            } catch (SQLException e) {
                connect.rollback();
                logger.error("Ошибка при добавлении '{}' пользователя: {}", name, e.getMessage(), e);
                throw new RuntimeException("Ошибка при добавлении пользователя в БД", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при добавлении пользователя в БД", e);
        }
    }
    
    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement pstm = connect.prepareStatement(sql)) {
                pstm.setLong(1, id);
                pstm.executeUpdate();
                connect.commit();
                logger.info("Пользователь {} удален", id);
            } catch (SQLException e) {
                connect.rollback();
                logger.error("При удалении пользователя с id={} произошла ошибка: {}", id, e.getMessage(), e);
                throw new RuntimeException("Ошибка при удалении пользователя", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement pstm = connect.prepareStatement(sql);
                 ResultSet resultSet = pstm.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("name"),
                            resultSet.getString("lastName"),
                            resultSet.getByte("age")
                    );
                    user.setId(resultSet.getLong("id"));
                    users.add(user);
                }
                connect.commit();
                logger.info("Общее количество загруженных пользователей: {}", users.size());
            } catch (SQLException e) {
                connect.rollback();
                logger.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
                throw new RuntimeException("Ошибка при получении списка пользователей", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка пользователей", e);
        }
        return users;
    }
    
    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (Connection connect = Util.getConnection()) {
            connect.setAutoCommit(false);
            try (PreparedStatement pstm = connect.prepareStatement(sql)) {
                pstm.executeUpdate();
                connect.commit();
                logger.info("Таблица очищена");
            } catch (SQLException e) {
                connect.rollback();
                logger.error("При очистке таблицы произошла ошибка: {}", e.getMessage(), e);
                throw new RuntimeException("Ошибка при очистке таблицы", e);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении соединения: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при очистке таблицы", e);
        }
    }
}
