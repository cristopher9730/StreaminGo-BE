package com.project.demo.rest.stream;


import com.project.demo.logic.entity.stream.StreamService;
import com.project.demo.logic.entity.stream.SubtitleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("stream")
public class StreamController {

    @Autowired
    private StreamService streamService;

    @GetMapping(value = "video", produces = "video/mp4")
    public Mono<Resource> streamContent(@RequestParam String title) {
        return streamService.retrieveContent(title);
    }

    @PostMapping(value = "subtitles", produces = "text/vtt")
    public Mono<Resource> streamSubtitles(@RequestBody SubtitleRequest subtitleRequest) {
        return streamService.retrieveSubtitles(subtitleRequest.getTitle(), subtitleRequest.getLang());
    }
}
