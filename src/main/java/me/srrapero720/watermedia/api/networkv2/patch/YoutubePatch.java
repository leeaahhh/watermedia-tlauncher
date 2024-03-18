package me.srrapero720.watermedia.api.networkv2.patch;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import me.srrapero720.watermedia.api.network.twitch.StreamQuality;
import me.srrapero720.watermedia.api.networkv2.DynamicURL;
import me.srrapero720.watermedia.api.networkv2.NetworkAPI;
import me.srrapero720.watermedia.api.networkv2.URLPatcher;
import me.srrapero720.watermedia.tools.exceptions.PatchingURLException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePatch extends URLPatcher {
    private static final Pattern PATTERN = Pattern.compile("(?:youtu\\.be/|youtube\\.com/(?:embed/|v/|shorts/|feeds/api/videos/|watch\\?v=|watch\\?.+&v=))([^/?&#]+)");

    @Override
    public String platform() {
        return "Youtube";
    }

    @Override
    public boolean isValid(DynamicURL url) {
        return url.isLocal() && url.asURL().getHost().endsWith("youtube.com") || url.asURL().getHost().endsWith("youtu.be");
    }

    @Override
    public DynamicURL patch(DynamicURL url, Quality preferQuality) throws PatchingURLException {
        super.patch(url, preferQuality);

        Matcher matcher = PATTERN.matcher(url.toString());
        if (matcher.find()) {
            try {
                String videoId = matcher.group(1);
                Response<VideoInfo> response = new YoutubeDownloader().getVideoInfo(new RequestVideoInfo(videoId));

                if (response == null) throw new NullPointerException("Response from Youtube is null");
                VideoInfo videoInfo = new YoutubeDownloader().getVideoInfo(new RequestVideoInfo(videoId)).data();
                VideoDetails videoDetails = videoInfo.details();

                if (videoDetails.isLive()) {
                    // LIVE STREAM
                    String ytLivePlaylist = fetchLivePlaylist(videoDetails.liveUrl());
                    if (ytLivePlaylist == null) throw new IllegalArgumentException("Live URL playlist is null");

                    // TODO: add quality support
                    NetworkAPI.createDynamicUrl(StreamQuality.parse(ytLivePlaylist).get(0).getUrl(), false, true);
                } else {
                    // TODO: add quality support
                    // BEST WITH AUDIO
                    VideoFormat bestWithAudio = videoInfo.bestVideoWithAudioFormat();
                    if (bestWithAudio != null) return NetworkAPI.createDynamicUrl(bestWithAudio.url(), true, false);

                    // TODO: add quality support
                    // WITHOUT AUDIO
                    VideoFormat bestWithoutAudio = videoInfo.bestVideoFormat();
                    if (bestWithoutAudio != null) return NetworkAPI.createDynamicUrl(bestWithoutAudio.url(), true, false);

                    // TODO: add quality support
                    // WITHOUT VIDEO
                    AudioFormat bestWithoutVideo = videoInfo.bestAudioFormat();
                    if (bestWithoutVideo != null) return NetworkAPI.createDynamicUrl(bestWithoutVideo.url(), true, false);
                }
            } catch (Exception e) {
                throw new PatchingURLException(url.getSource(), e);
            }
        }

        return url;
    }

    private String fetchLivePlaylist(String url) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) return null;

        InputStream inputStream = conn.getInputStream();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}