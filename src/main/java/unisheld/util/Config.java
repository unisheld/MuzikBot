package unisheld.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Утилитарный класс для чтения конфигурационных данных из файла свойств.
 */
public class Config {
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";

    /**
     * Получает токен бота из файла свойств.
     *
     * @return Токен бота или null, если произошла ошибка при чтении файла.
     */
    public static String getBotToken() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(fileInputStream);
            return properties.getProperty("discord.bot.token");
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Обработка ошибки
        }
    }
}
