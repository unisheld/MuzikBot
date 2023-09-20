package unisheld.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import unisheld.audio.GuildMusicManager;

import java.util.Map;
import java.util.Objects;

/**
 * Команда для воспроизведения музыки в голосовом канале Discord.
 */
public class PlayCommand {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    /**
     * Создает новый экземпляр команды для воспроизведения музыки.
     *
     * @param playerManager Менеджер аудио-плеера.
     * @param musicManagers Карта для хранения менеджеров музыки для каждой гильдии.
     */
    public PlayCommand(AudioPlayerManager playerManager, Map<Long, GuildMusicManager> musicManagers) {
        this.playerManager = playerManager;
        this.musicManagers = musicManagers;
    }

    /**
     * Выполняет команду для воспроизведения музыки в голосовом канале Discord.
     *
     * @param event    Событие сообщения, содержащее команду.
     * @param trackUrl URL аудиодорожки или плейлиста для воспроизведения.
     */
    public void execute(MessageReceivedEvent event, String trackUrl) {
        Guild guild = event.getGuild();
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);

        AudioManager audioManager = guild.getAudioManager();
        AudioChannelUnion voiceChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

        if (voiceChannel != null) {
            if (!audioManager.isSelfDeafened()) {
                // Устанавливаем отправщика аудио для AudioManager
                audioManager.setSendingHandler(musicManager.getSendHandler());
                // Открываем аудио-соединение
                audioManager.openAudioConnection(voiceChannel);
                System.out.println("Бот подключен к голосовому каналу: " + voiceChannel.getName());

                playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        System.out.println("Загружена аудиодорожка: " + track.getInfo().title); // Выводим информацию о загруженной аудиодорожке
                        play(musicManager, track);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        AudioTrack firstTrack = playlist.getSelectedTrack();

                        if (firstTrack == null) {
                            firstTrack = playlist.getTracks().get(0);
                        }

                        System.out.println("Загружен плейлист: " + playlist.getName()); // Выводим информацию о загруженном плейлисте
                        play(musicManager, firstTrack);
                    }

                    @Override
                    public void noMatches() {
                        MessageChannel channel = event.getChannel();
                        channel.sendMessage("По запросу " + trackUrl + " ничего не найдено.").queue();
                        System.out.println("По запросу " + trackUrl + " ничего не найдено."); // Выводим сообщение в консоль
                    }

                    @Override
                    public void loadFailed(FriendlyException exception) {
                        MessageChannel channel = event.getChannel();
                        channel.sendMessage("Не удалось воспроизвести: " + exception.getMessage()).queue();
                        System.out.println("Не удалось воспроизвести: " + exception.getMessage()); // Выводим сообщение в консоль
                    }
                });
            } else {
                event.getChannel().sendMessage("Бот заблокирован и не имеет доступа к микрофону.").queue();
                System.out.println("Бот заблокирован и не имеет доступа к микрофону.");
            }
        } else {
            event.getChannel().sendMessage("Пожалуйста, присоединьтесь к голосовому каналу перед использованием этой команды.").queue();
            System.out.println("Пользователь не находится в голосовом канале.");
            // Прерываем выполнение метода, если пользователь не находится в голосовом канале
        }
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager musicManager = new GuildMusicManager(playerManager);
            guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
            return musicManager;
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);

            System.out.println("Начало воспроизведения аудиодорожки: " + track.getInfo().title); // Добавляем вывод в консоль
        } else {
            musicManager.scheduler.queue(track);
            System.out.println("Добавлена аудиодорожка в очередь воспроизведения: " + track.getInfo().title); // Добавляем вывод в консоль
        }
    }
}
