package jm.task.core.jdbc.service;

import java.util.List;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.model.User;

import static jm.task.core.jdbc.dao.UserDaoJDBCImpl.logger;


public class UserServiceImpl implements UserService {
    
    private final UserDao userDao;
    
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void createUsersTable() {
        try {
            userDao.createUsersTable();
            logger.info("Создали таблицу пользователей или она уже существует");
        } catch (Exception e) {
            logger.error("При создании таблицы произошла ошибка; {} ", e.getMessage(), e);
            throw e;
        }
    }
    
    public void dropUsersTable() {
        try {
            userDao.dropUsersTable();
            logger.info("Таблица удалена");
        } catch (Exception e) {
            logger.error("Ошибка при удалении таблицы: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public void saveUser(String name, String lastName, byte age) {
        try {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Имя не заполнено");
            }
            if (lastName == null || lastName.isBlank()) {
                throw new IllegalArgumentException("Фамилия  не указана");
            }
            if (age < 0 || age > 130) {
                throw new IllegalArgumentException("Возраст не корректный");
            }
            userDao.saveUser(name, lastName, age);
            logger.info("Добавлен пользователь '{}' '{}' , возраст {}", name, lastName, age);
        } catch (Exception e) {
            logger.error(" При сохранении пользователя произошла ошибка '{} {}': {}", name, lastName, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void removeUserById(long id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("ID не бывает отрицателным");
            }
            userDao.removeUserById(id);
            logger.info("Пользователь с ID {}  удалён.", id);
        } catch (Exception e) {
            logger.error("Ошибка при удалении  ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        try {
            List<User> users = userDao.getAllUsers();
            logger.info("Загружено {} пользователей.", users.size());
            return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    @Override
    public void cleanUsersTable() {
        try {
            userDao.cleanUsersTable();
            logger.info("Таблица  чистая.");
        } catch (Exception e) {
            logger.error("Ошибка при очистке таблицы: {}", e.getMessage(), e);
            throw e;
        }
    }
}
