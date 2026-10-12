package ma.xproce.video_service;

import ma.xproce.video_service.entities.Creator;
import ma.xproce.video_service.entities.Video;
import ma.xproce.video_service.repositories.CreatorRepository;
import ma.xproce.video_service.repositories.VideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class VideoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CreatorRepository creatorRepository, VideoRepository videoRepository) {
        return args -> {
            String datePublication = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            List<Creator> creators = creatorRepository.saveAll(List.of(
                    Creator.builder().name("Alice Martin").email("alice@example.com").build(),
                    Creator.builder().name("Bruno Dupont").email("bruno@example.com").build(),
                    Creator.builder().name("Carle Bernard").email("carle@example.com").build()
            ));

            List<Video> videos = List.of(
                    Video.builder()
                            .name("Introduction a Spring Boot")
                            .url("https://example.com/videos/spring-boot")
                            .description("Decouverte de Spring Boot et de JPA.")
                            .datePublication(datePublication)
                            .creator(creators.get(0))
                            .build(),
                    Video.builder()
                            .name("Guide GraphQL")
                            .url("https://example.com/videos/graphql")
                            .description("Creation d'une API GraphQL avec Spring.")
                            .datePublication(datePublication)
                            .creator(creators.get(1))
                            .build(),
                    Video.builder()
                            .name("Relations JPA")
                            .url("https://example.com/videos/jpa-relations")
                            .description("Comprendre les relations entre entites JPA.")
                            .datePublication(datePublication)
                            .creator(creators.get(2))
                            .build()
            );

            videoRepository.saveAll(videos);
        };
    }
}
