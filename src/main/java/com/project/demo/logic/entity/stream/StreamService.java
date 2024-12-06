package com.project.demo.logic.entity.stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StreamService {

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String FILE_PATH = "classpath:content//%s.mp4";
    private static final String SUBTITLE_PATH = "classpath:content/%s_%s.vtt";

    public StreamService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Mono<Resource> retrieveContent(String title){
        return Mono.fromSupplier(() -> resourceLoader.getResource(String.format(FILE_PATH, title)));
    }
    public Mono<Resource> retrieveSubtitles(String title, String lang) {
        String path = String.format(SUBTITLE_PATH, title, lang);
        return Mono.fromSupplier(() -> resourceLoader.getResource(path));
    }
}
