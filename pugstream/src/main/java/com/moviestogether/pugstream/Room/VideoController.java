package com.moviestogether.pugstream.Room;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/videoplayer")
public class VideoController {

    private String extractYouTubeVideoId(String videoLink) {
        String videoId = null;

        String[] parts = videoLink.split("\\?|&");
        for (String part : parts) {
            if (part.startsWith("v=")) {
                videoId = part.substring(2);
                break;
            }
        }

        return videoId;
    }

    @PostMapping("/play")
    public String playVideo(@RequestParam(name = "videoLink") String videoLink, Model model) {
        String embedCode = null;
        if (videoLink != null && videoLink.startsWith("https://www.youtube.com/")) { // Just for YouTube videos. Subject to change
            String videoId = extractYouTubeVideoId(videoLink);

            // Need to be adjusted to media player
            embedCode = "<iframe src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>";
        }
        // Movie list need to be added

        model.addAttribute("embedCode", embedCode);

        return "video_player";
    }

    //For testing purposes
//    @GetMapping("/play")
//    public String playVideo(Model model) {
//        String videoLink = "https://www.youtube.com/watch?v=UfagTKOA_h4";
//        String embedCode = null;
//        String videoId = extractYouTubeVideoId(videoLink);
//        if (videoId != null && videoLink.startsWith("https://www.youtube.com/")) {
//            embedCode = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>";
//        }
//        model.addAttribute("embedCode", embedCode);
//        return "video_player";
//    }

}
