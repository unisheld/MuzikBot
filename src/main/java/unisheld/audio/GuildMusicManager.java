package unisheld.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Хранитель для аудио-плеера и планировщика треков для одной гильдии (сервера).
 */
public class GuildMusicManager {
    /**
     * Аудио-плеер для гильдии (сервера).
     */
    public final AudioPlayer player;
    /**
     * Планировщик треков для плеера.
     */
    public final TrackScheduler scheduler;

    /**
     * Создает аудио-плеер и планировщик треков для гильдии.
     *
     * @param manager Аудио-менеджер для создания плеера.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    /**
     * Получает обертку вокруг AudioPlayer для использования как AudioSendHandler.
     *
     * @return Обертка вокруг AudioPlayer, чтобы использовать его как AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
