package com.stream.app.spring_stream_backend.controllers;

import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.payload.CustomResponse;
import com.stream.app.spring_stream_backend.services.VideoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/save")
    public ResponseEntity<CustomResponse> saveVideo(
            @RequestBody()MultipartFile file,
            @RequestParam("videoTitle")String videoTitle,
            @RequestParam("videoDescription")String videoDescription
            ) {

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setVideoTitle(videoTitle);
        videoEntity.setVideoDescription(videoDescription);

        videoService.saveVideo(videoEntity, file);

        return null;
    }

}
