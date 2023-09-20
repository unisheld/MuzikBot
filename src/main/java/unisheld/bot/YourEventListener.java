package unisheld.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import unisheld.commands.CommandHandler;

/**
 * Этот класс является слушателем событий для обработки входящих сообщений.
 */
public class YourEventListener extends ListenerAdapter {
    private final CommandHandler commandHandler;

    /**
     * Создает новый экземпляр класса YourEventListener.
     *
     * @param commandHandler Обработчик команд для обработки входящих команд.
     */
    public YourEventListener(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Обрабатывает событие при получении нового сообщения.
     *
     * @param event Событие сообщения.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String message = event.getMessage().getContentRaw();

            // Выводим в консоль входящее сообщение
            System.out.println("Получено сообщение от пользователя " + event.getAuthor().getName() + ": " + message);

            if (message.startsWith("/")) { // Проверяем, начинается ли сообщение с "/"
                // Разделяем команду и аргументы
                String[] commandArgs = message.split("\\s+");
                String commandName = commandArgs[0].substring(1); // Убираем "/" из названия команды

                // Вызываем handleCommand с соответствующими аргументами
                commandHandler.handleCommand(event, commandName, commandArgs);
            }
        }
    }
}
