package unisheld.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Этот класс планирует треки для аудио-плеера. Он содержит очередь треков.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    /**
     * Создает новый объект TrackScheduler для заданного аудио-плеера.
     *
     * @param player Аудио-плеер, который использует этот планировщик.
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Добавить следующий трек в очередь или начать воспроизведение сразу, если очередь пуста.
     *
     * @param track Трек для воспроизведения или добавления в очередь.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
            System.out.println("Добавлен трек в очередь: " + track.getInfo().title);
        } else {
            System.out.println("Начато воспроизведение трека: " + track.getInfo().title);
        }
    }

    /**
     * Начать воспроизведение следующего трека, остановив текущий, если он играет.
     */
    public void nextTrack() {
        AudioTrack track = queue.poll();
        if (track != null) {
            player.startTrack(track, false);
            System.out.println("Начато воспроизведение следующего трека: " + track.getInfo().title);
        } else {
            player.stopTrack(); // Останавливаем текущий трек
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Начать следующий трек только в случае, если причина окончания подходящая (FINISHED или LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}
