package unisheld.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import unisheld.audio.AudioPlayerManagerProvider;
import unisheld.audio.GuildMusicManager;
import unisheld.commands.CommandHandler;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class DiscordBot {
    private final String token;

    public DiscordBot(String token) {
        this.token = token;
    }

    public void start() throws LoginException {
        try {
            // Создаем мапу для хранения GuildMusicManager для каждой гильдии (сервера).
            Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

            // Создаем AudioPlayerManager для управления аудио.
            AudioPlayerManager playerManager = AudioPlayerManagerProvider.createPlayerManager();
            System.out.println("Инициализация AudioPlayerManager и GuildMusicManager...");

            // Создаем объект для обработки команд.
            CommandHandler commandHandler = new CommandHandler(musicManagers, playerManager);

            JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(new YourEventListener(commandHandler))
                    .setActivity(Activity.playing("potok botok"))
                    .setStatus(OnlineStatus.ONLINE)
                    .build();

            System.out.println("Бот успешно запущен!");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
