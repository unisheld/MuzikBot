package unisheld;

import unisheld.bot.DiscordBot;
import unisheld.util.Config;

import javax.security.auth.login.LoginException;

/**
 * Главный класс приложения для инициализации и запуска бота Discord.
 */
public class Main {
    public static void main(String[] args) {
        // Получение токена Discord из конфигурационного файла
        String discordToken = Config.getBotToken();

        if (discordToken != null) {
            try {
                // Инициализируйте и запустите ваш бот с использованием токена Discord
                DiscordBot bot = new DiscordBot(discordToken);
                bot.start();
            } catch (LoginException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Не удалось получить токен Discord из конфигурации.");
        }
    }
}
