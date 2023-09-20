package unisheld.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import unisheld.audio.GuildMusicManager;

import java.util.Map;

/**
 * Команда для пропуска текущего трека и отключения бота от голосового канала Discord.
 */
public class SkipCommand {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    /**
     * Создает новый экземпляр команды для пропуска трека и отключения бота от голосового канала.
     *
     * @param playerManager Менеджер аудио-плеера.
     * @param musicManagers Карта для хранения менеджеров музыки для каждой гильдии.
     */
    public SkipCommand(AudioPlayerManager playerManager, Map<Long, GuildMusicManager> musicManagers) {
        this.playerManager = playerManager;
        this.musicManagers = musicManagers;
    }

    /**
     * Выполняет команду для пропуска текущего трека и отключения бота от голосового канала Discord.
     *
     * @param event Событие сообщения, содержащее команду.
     */
    public void execute(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild.getIdLong());

        musicManager.scheduler.nextTrack();

        guild.getAudioManager().closeAudioConnection();

        // Выводим сообщение в консоль
        System.out.println("Пропущен текущий трек в сервере: " + guild.getName());

        // Отправляем сообщение в текстовый канал
        event.getChannel().sendMessage("Пропущен текущий трек и бот отключен от голосового канала.").queue();
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
