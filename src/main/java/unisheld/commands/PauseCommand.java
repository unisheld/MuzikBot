package unisheld.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import unisheld.audio.GuildMusicManager;

import java.util.Map;

/**
 * Команда для приостановки воспроизведения музыки в голосовом канале Discord.
 */
public class PauseCommand {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    /**
     * Создает новый экземпляр команды для приостановки воспроизведения музыки.
     *
     * @param playerManager Менеджер аудио-плеера.
     * @param musicManagers Карта для хранения менеджеров музыки для каждой гильдии.
     */
    public PauseCommand(AudioPlayerManager playerManager, Map<Long, GuildMusicManager> musicManagers) {
        this.playerManager = playerManager;
        this.musicManagers = musicManagers;
    }

    /**
     * Выполняет команду для приостановки воспроизведения музыки в голосовом канале.
     *
     * @param event Событие сообщения, содержащее команду.
     */
    public void execute(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild.getIdLong());

        if (musicManager.player.isPaused()) {
            event.getChannel().sendMessage("Плеер уже на паузе.").queue();
            System.out.println("Плеер уже на паузе в сервере: " + guild.getName());
        } else {
            musicManager.player.setPaused(true);
            event.getChannel().sendMessage("Плеер теперь на паузе.").queue();
            System.out.println("Плеер был поставлен на паузу в сервере: " + guild.getName());
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(long guildId) {
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        return musicManager;
    }
}
