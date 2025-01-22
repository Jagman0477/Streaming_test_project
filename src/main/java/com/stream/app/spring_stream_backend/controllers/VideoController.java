package com.stream.app.spring_stream_backend.controllers;

import com.stream.app.spring_stream_backend.constants.CommonConstants;
import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.models.Response;
import com.stream.app.spring_stream_backend.services.VideoService;
import com.stream.app.spring_stream_backend.services.implementations.videoServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin(origins = "http://localhost:4200")
public class VideoController {

    private VideoService videoService;
    private static final Logger LOGGER = LoggerFactory.getLogger(videoServiceImpl.class);

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

    @GetMapping("/watch/{videoId}")
    public ResponseEntity<?> stream(
            @PathVariable String videoId,
            @RequestHeader(value = "Range", required = false) String range){
        return videoService.streamVideo(range, videoId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoEntity>> getAllVideos() {
        List<VideoEntity> videoList = videoService.getAllVideos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(videoList);
    }

}
