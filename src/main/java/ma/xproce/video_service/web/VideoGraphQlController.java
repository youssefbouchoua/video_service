package ma.xproce.video_service.web;


import ma.xproce.video_service.dtos.CreatorRequest;
import ma.xproce.video_service.dtos.VideoRequest;
import ma.xproce.video_service.entities.Creator;
import ma.xproce.video_service.entities.Video;
import ma.xproce.video_service.repositories.CreatorRepository;
import ma.xproce.video_service.repositories.VideoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class VideoGraphQlController {
    private final CreatorRepository creatorRepository;
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;

    public VideoGraphQlController(CreatorRepository creatorRepository, VideoRepository videoRepository,
                                  ModelMapper modelMapper) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
    }

    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {
        return creatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Creator %s not found", id)));
    }

    @MutationMapping
    public Creator saveCreator(@Argument CreatorRequest creator) {
        Creator creatorEntity = modelMapper.map(creator, Creator.class);
        return creatorRepository.save(creatorEntity);
    }

    @MutationMapping
    public Video saveVideo(@Argument VideoRequest video) {
        Creator creatorEntity = creatorRepository.save(
                modelMapper.map(video.getCreator(), Creator.class));
        Video videoEntity = modelMapper.map(video, Video.class);
        videoEntity.setCreator(creatorEntity);
        return videoRepository.save(videoEntity);
    }

    @SubscriptionMapping
    public Flux<Video> notifyVideoChange() {
        return Flux.interval(Duration.ofSeconds(1))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> {
                    Creator creator = creatorRepository.save(Creator.builder()
                            .name("creator-" + ThreadLocalRandom.current().nextInt(1000))
                            .email("creator@example.com")
                            .build());

                    Video video = videoRepository.findById(1L)
                            .orElseThrow(() -> new RuntimeException("Video 1 not found"));
                    video.setCreator(creator);
                    return videoRepository.save(video);
                });
    }
}
