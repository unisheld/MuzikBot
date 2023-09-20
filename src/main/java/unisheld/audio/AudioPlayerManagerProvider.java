package unisheld.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

/**
 * Поставщик AudioPlayerManager для создания и настройки экземпляра AudioPlayerManager.
 */
public class AudioPlayerManagerProvider {
    private static AudioPlayerManager playerManager;

    /**
     * Создает и настраивает AudioPlayerManager при первом вызове, а затем возвращает его.
     *
     * @return Экземпляр AudioPlayerManager.
     */
    public static synchronized AudioPlayerManager createPlayerManager() {
        if (playerManager == null) {
            playerManager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(playerManager);
            // Регистрация других источников, если необходимо
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());

            System.out.println("Создан AudioPlayerManager и зарегистрирован YoutubeAudioSourceManager");
        }
        System.out.println("Создан AudioPlayerManager");
        return playerManager;
    }
}
