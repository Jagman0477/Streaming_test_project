package com.stream.app.spring_stream_backend.controllers;

import com.stream.app.spring_stream_backend.constants.CommonConstants;
import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.models.Response;
import com.stream.app.spring_stream_backend.services.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {

    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveVideo(
            @RequestBody()MultipartFile file,
            @RequestParam("videoTitle")String videoTitle,
            @RequestParam("videoDescription")String videoDescription
            ) {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setVideoTitle(videoTitle);
        videoEntity.setVideoDescription(videoDescription);
        VideoEntity savedVideo = videoService.saveVideo(videoEntity, file);
        if(null != savedVideo){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(CommonConstants.SUCCESS, CommonConstants.VIDEO_SAVED_SUCCESSFULLY,
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), LocalDateTime.now().toString()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(CommonConstants.FAILURE, CommonConstants.VIDEO_NOT_UPLOADED,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), LocalDateTime.now().toString()));
        }
    }

}
