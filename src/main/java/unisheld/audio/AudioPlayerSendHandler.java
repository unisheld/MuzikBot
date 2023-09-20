package unisheld.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

/**
 * Это обертка вокруг AudioPlayer, которая делает его совместимым с AudioSendHandler для JDA.
 * Поскольку JDA вызывает canProvide перед каждым вызовом provide20MsAudio(),
 * мы извлекаем фрейм в canProvide() и используем фрейм, который мы уже извлекли, в provide20MsAudio().
 */
public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    /**
     * Создает новый объект AudioPlayerSendHandler для заданного аудио-плеера.
     *
     * @param audioPlayer Аудио-плеер для обертки.
     */
    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Проверяет, можно ли предоставить аудио-фрейм для отправки.
     *
     * @return true, если аудио-фрейм доступен, в противном случае false.
     */
    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    /**
     * Предоставляет аудио-фрейм для отправки.
     *
     * @return ByteBuffer с аудио-данными.
     */
    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    /**
     * Проверяет, поддерживается ли формат Opus.
     *
     * @return true, если формат Opus поддерживается, в противном случае false.
     */
    @Override
    public boolean isOpus() {
        return true;
    }
}
