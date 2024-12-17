package com.stream.app.spring_stream_backend.services.implementations;

import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.repositories.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class videoServiceImpl implements VideoService {

    Logger LOGGER = LoggerFactory.getLogger(videoServiceImpl.class);

    private VideoRepository videoRepository;

    @Value("${files.videos}")
    private String saveDir;

    videoServiceImpl(VideoRepository videoRepository){
        this.videoRepository = videoRepository;
    }

    @Override
    public VideoEntity getVideoById(Long videoId) {
        return null;
    }

    @Override
    public VideoEntity saveVideo(VideoEntity videoEntity, MultipartFile videoFile) {

        try {
            // get file metadata
            String fileName = videoFile.getOriginalFilename();
            InputStream fileStream = videoFile.getInputStream();
            Long fileSize = videoFile.getSize();
            String contentType = videoFile.getContentType();

            // path for file saving
            String cleanFileName = StringUtils.cleanPath(fileName);
            Path filePath = Paths.get(saveDir, cleanFileName);
            LOGGER.info("FILE PATH -------- " + filePath);

        } catch(IOException e) {

        }

        return null;
    }

    @Override
    public List<VideoEntity> getAllVideos() {
        return null;
    }

    @Override
    public VideoEntity getVideoByTitle(String videoTitle) {
        return null;
    }
}
