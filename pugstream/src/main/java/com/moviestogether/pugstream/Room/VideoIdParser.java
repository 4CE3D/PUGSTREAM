package com.moviestogether.pugstream.Room;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VideoIdParser {

    public String extractId(String url) {
        String videoId, embedCode = null;
        if (url != null) {
            if (url.contains(".mp4")) {
                embedCode = "<iframe src=\"" + url + "\" frameborder=\"0\" allowfullscreen></iframe>";
            }
            if (url.contains("youtube.com") || url.contains("youtu.be")) {
                videoId = extractYouTubeVideoId(url);
                // Need to be adjusted to media player
                embedCode = "<iframe src=\"https://www.youtube.com/embed/" + videoId + "\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
            }
            if (url.contains("vimeo.com")) {
                videoId = extractVimeoVideoId(url);
                // Need to be adjusted to media player
                embedCode = "<iframe src=\"https://player.vimeo.com/video/" + videoId + "\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
            }
            if (url.contains("twitch.tv")) {
                videoId = extractTwitchVideoId(url);
                // Need to be adjusted to media player. Need "parent" to be added
                embedCode = "<iframe src=\"https://player.twitch.tv/?video=" + videoId + "&parent=\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
            }
            if (url.contains("tiktok.com")) {
                if (url.contains("vm.")) {
                    System.out.println("Please, don't use app url"); // Needs to be changed
                }
                videoId = extractTiktokVideoId(url);
                // Need to be adjusted to media player
                embedCode = "<iframe src=\"https://www.tiktok.com/embed/v2/" + videoId + "\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
            }
        } else {
            throw new IllegalStateException("No URL provided");
        }
        return embedCode;
    }

    private String extractYouTubeVideoId(String url) {
        String videoId = null;
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2Fvideos%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }

    private String extractVimeoVideoId(String url) {
        String videoId = null;
        if (url.contains("vimeo.com")) {
            String pattern = "(?:vimeo.com\\/|video\\/)(\\d+)";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group(1);
            }
        }
        return videoId;
    }

    private String extractTwitchVideoId(String url) {
        String videoId = null;
        if (url.contains("twitch.tv")) {
            String pattern = "(?<=videos\\/|v\\/|\\/videos\\/|\\/v\\/|clip\\/|\\/clip\\/|videos%2F|v%2F|clip%2F|embed\\/)\\d+";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group();
            }
        }
        return videoId;
    }

    private String extractTiktokVideoId(String url) {
        String videoId = null;
        if (url.contains("tiktok.com")) {
            String pattern = "(?:tiktok\\.com\\/.*\\/|\\/)(\\d+)";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);
            if (matcher.find()) {
                videoId = matcher.group(1);
            }
        }
        return videoId;
    }
}