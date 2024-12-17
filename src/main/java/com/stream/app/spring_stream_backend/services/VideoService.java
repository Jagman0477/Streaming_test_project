package com.stream.app.spring_stream_backend.services;

import com.stream.app.spring_stream_backend.entities.VideoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    // get single video
    VideoEntity getVideoById(Long videoId);

    // save video
    VideoEntity saveVideo(VideoEntity videoEntity, MultipartFile videoFile);

    // get a list all videos
    List<VideoEntity> getAllVideos();

    // get a video by title
    VideoEntity getVideoByTitle(String videoTitle);

}
