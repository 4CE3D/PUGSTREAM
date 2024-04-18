package com.moviestogether.pugstream.Room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(maxAge = 3600)
@RequestMapping("/videoplayer")
public class VideoController {
    private final VideoIdParser videoIdParser;

    @Autowired
    public VideoController(VideoIdParser videoIdParser) {
        this.videoIdParser = videoIdParser;
    }

    @PostMapping("/play")
    public String playVideo(@RequestParam(name = "videoLink") String videoLink, Model model) {
        String embedCode = videoIdParser.extractId(videoLink);
        model.addAttribute("embedCode", embedCode);
        return "video_player";
    }

    //For testing purposes
//    @GetMapping("/play")
//    public String playVideo(Model model) {
//        String videoLink = "https://www.youtube.com/watch?v=UfagTKOA_h4";
//        String embedCode = videoIdParser.extractId(videoLink);
//        model.addAttribute("embedCode", embedCode);
//        return "video_player";
//    }

}
