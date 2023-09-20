package unisheld.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import unisheld.audio.GuildMusicManager;

import java.util.Map;

/**
 * Команда для возобновления воспроизведения музыки в голосовом канале Discord.
 */
public class ResumeCommand {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    /**
     * Создает новый экземпляр команды для возобновления воспроизведения музыки.
     *
     * @param playerManager Менеджер аудио-плеера.
     * @param musicManagers Карта для хранения менеджеров музыки для каждой гильдии.
     */
    public ResumeCommand(AudioPlayerManager playerManager, Map<Long, GuildMusicManager> musicManagers) {
        this.playerManager = playerManager;
        this.musicManagers = musicManagers;
    }

    /**
     * Выполняет команду для возобновления воспроизведения музыки в голосовом канале Discord.
     *
     * @param event Событие сообщения, содержащее команду.
     */
    public void execute(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild.getIdLong());

        if (!musicManager.player.isPaused()) {
            event.getChannel().sendMessage("Плеер не на паузе.").queue();
        } else {
            musicManager.player.setPaused(false);
            event.getChannel().sendMessage("Плеер теперь продолжает воспроизведение.").queue();
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
