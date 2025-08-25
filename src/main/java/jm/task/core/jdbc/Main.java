import jm.task.core.jdbc.util.Util;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    
    public static void main(String[] args) {
        try (Connection connection = Util.getConnection()) {
            logger.info("Подключение с базой установлено");
        } catch (SQLException e) {
            logger.severe("Нет подключения к базе: " + e.getMessage());
        }
    }
}
