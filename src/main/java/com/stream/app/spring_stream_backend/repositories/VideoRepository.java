package com.stream.app.spring_stream_backend.repositories;

import com.stream.app.spring_stream_backend.entities.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    Optional<VideoEntity> findByVideoTitle(String videoTitle);

}
