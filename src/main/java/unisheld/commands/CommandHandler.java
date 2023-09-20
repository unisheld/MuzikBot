package unisheld.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Map;
import unisheld.audio.GuildMusicManager;

/**
 * Обработчик команд для бота. Он определяет, какую команду вызвал пользователь и выполняет соответствующее действие.
 */
public class CommandHandler {
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager playerManager;

    /**
     * Создает новый экземпляр класса CommandHandler.
     *
     * @param musicManagers Карта для хранения менеджеров музыки для каждой гильдии.
     * @param playerManager Менеджер аудио-плеера.
     */
    public CommandHandler(Map<Long, GuildMusicManager> musicManagers, AudioPlayerManager playerManager) {
        this.musicManagers = musicManagers;
        this.playerManager = playerManager;
    }

    /**
     * Обрабатывает входящую команду и вызывает соответствующую команду.
     *
     * @param event   Событие сообщения, содержащее команду.
     * @param command Название команды.
     * @param args    Аргументы команды.
     */
    public void handleCommand(MessageReceivedEvent event, String command, String[] args) {
        // Проверяем, какая команда была вызвана
        switch (command.toLowerCase()) {
            case "play" -> new PlayCommand(playerManager, musicManagers).execute(event, args[1]);
            case "stop" -> new StopCommand(playerManager, musicManagers).execute(event);
            case "pause" -> new PauseCommand(playerManager, musicManagers).execute(event);
            case "resume" -> new ResumeCommand(playerManager, musicManagers).execute(event);
            case "skip" -> new SkipCommand(playerManager, musicManagers).execute(event);

            // Другие команды могут быть добавлены по аналогии
            default -> {
            }
            // Обработка ошибки: неизвестная команда
        }
    }
}
