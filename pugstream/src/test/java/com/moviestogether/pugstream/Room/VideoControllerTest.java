package com.moviestogether.pugstream.Room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VideoControllerTest {

    @InjectMocks
    private VideoController videoController;

    @Mock
    private VideoIdParser videoIdParser;

    @Mock
    private Model model;

    @Test
    public void playVideo_ReturnsCorrectView() {
        String videoLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String embedCode = "expected-embed-code";
        when(videoIdParser.extractId(videoLink)).thenReturn(embedCode);

        String viewName = videoController.playVideo(videoLink, model);

        verify(model).addAttribute("embedCode", embedCode);
        assertThat(viewName).isEqualTo("video_player");
    }
}
