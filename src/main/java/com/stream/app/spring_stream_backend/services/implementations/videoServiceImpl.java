package com.stream.app.spring_stream_backend.services.implementations;

import com.stream.app.spring_stream_backend.constants.CommonConstants;
import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.repositories.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class videoServiceImpl implements VideoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(videoServiceImpl.class);
    private VideoRepository videoRepository;
    @Value("${files.videos}")
    private String saveDir;

    @PostConstruct
    public void init() {
        LOGGER.info("VideoService initialized after constructor call.");
        File dirCheckFile = new File(saveDir);

        // Save folder check else create
        if(!dirCheckFile.exists()){
            dirCheckFile.mkdir();
            LOGGER.debug("Save folder Created.");
        } else {
            LOGGER.debug("Save folder already exists.");
        }

    }

    @Autowired
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
            LOGGER.debug("File Path -> " + filePath);

            // save file to path
            Files.copy(fileStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.debug("Video copy saved.");
            // create corresponding Video Entity
            videoEntity.setContentType(videoFile.getContentType());
            videoEntity.setFilePath(filePath.toString());
            // save video entity
            videoRepository.save(videoEntity);
            LOGGER.debug("Video entity saved.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new com.stream.app.spring_stream_backend.exceptions.IOException(Objects.isNull(e.getMessage()) ? CommonConstants.IO_EXCEPTION_MESSAGE :
                    e.getMessage());
        }
        return videoEntity;
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
