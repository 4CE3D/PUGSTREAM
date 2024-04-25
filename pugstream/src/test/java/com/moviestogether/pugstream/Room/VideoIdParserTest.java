package com.moviestogether.pugstream.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class VideoIdParserTest {

    private VideoIdParser videoIdParser;

    @BeforeEach
    public void setup() {
        videoIdParser = new VideoIdParser();
    }

    @Test
    public void extractId_ReturnsYouTubeEmbedCode() {
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String expected = "<iframe src=\"https://www.youtube.com/embed/dQw4w9WgXcQ\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ReturnsYouTubeShortLinkEmbedCode() {
        String url = "https://youtu.be/dQw4w9WgXcQ";
        String expected = "<iframe src=\"https://www.youtube.com/embed/dQw4w9WgXcQ\" height=\"100%\" width=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ReturnsVimeoEmbedCode() {
        String url = "https://vimeo.com/12345678";
        String expected = "<iframe src=\"https://player.vimeo.com/video/12345678\" width=\"100%\" height=\"100%\" frameborder=\"0\" allowfullscreen></iframe>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ReturnsTwitchEmbedCode() {
        String url = "https://www.twitch.tv/videos/123456789";
        String expected = "<iframe src=\"https://player.twitch.tv/?video=123456789\" frameborder=\"0\" allowfullscreen=\"true\" scrolling=\"no\" height=\"378\" width=\"620\"></iframe>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ReturnsTikTokEmbedCode() {
        String url = "https://www.tiktok.com/@user/video/1234567890123456789";
        String expected = "<blockquote class=\"tiktok-embed\" cite=\"https://www.tiktok.com/@user/video/1234567890123456789\" data-video-id=\"1234567890123456789\" style=\"max-width: 605px;min-width: 325px;\" > <section> </section> </blockquote> <script async src=\"https://www.tiktok.com/embed.js\"></script>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ReturnsMP4EmbedCode() {
        String url = "https://example.com/videos/video.mp4";
        String expected = "<iframe src=\"https://example.com/videos/video.mp4\" frameborder=\"0\" allowfullscreen></iframe>";
        assertThat(videoIdParser.extractId(url)).isEqualTo(expected);
    }

    @Test
    public void extractId_ThrowsExceptionForNullInput() {
        Exception exception = assertThrows(IllegalStateException.class, () -> videoIdParser.extractId(null));
        assertEquals("No URL provided", exception.getMessage());
    }

    @Test
    public void extractId_ThrowsExceptionForEmptyString() {
        Exception exception = assertThrows(IllegalStateException.class, () -> videoIdParser.extractId(""));
        assertEquals("No URL provided", exception.getMessage());
    }
}
