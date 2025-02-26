package com.stream.app.spring_stream_backend.services.implementations;

import com.stream.app.spring_stream_backend.constants.CommonConstants;
import com.stream.app.spring_stream_backend.entities.VideoEntity;
import com.stream.app.spring_stream_backend.models.Response;
import com.stream.app.spring_stream_backend.repositories.VideoRepository;
import com.stream.app.spring_stream_backend.services.VideoService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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
        return videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not Found."));
    }

    @Override
    public VideoEntity saveVideo(VideoEntity videoEntity, MultipartFile videoFile) {
        try {
            // get file metadata
             String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + videoFile.getOriginalFilename();
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
        return  videoRepository.findAll();
    }

    @Override
    public VideoEntity getVideoByTitle(String videoTitle) {
        return null;
    }

    public ResponseEntity<?> streamVideo(String range, String videoId) {
        VideoEntity video = getVideoById(Long.valueOf(videoId));
        String filePath = video.getFilePath();
        String contentType = video.getContentType();
        Resource resource = new FileSystemResource(filePath);
        Long fileLength = null;
        try {
            fileLength = resource.contentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> ranges = new ArrayList<String>();
        if(Objects.isNull(contentType)){
            contentType = "application/octet-stream";
        }

        if(range == null){
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }

        // Calculate video range.
        ranges = Arrays.stream(range.replace("bytes=", "").split("-")).toList();

        Long rangeStart = Long.parseLong(ranges.get(0));

        Long rangeEnd = rangeStart + CommonConstants.CHUNK_SIZE - 1;
        if(rangeEnd >= fileLength)
            rangeEnd = fileLength - 1;

        InputStream rangedVideoStream;
        try {
            rangedVideoStream = Files.newInputStream(Paths.get(filePath));
            rangedVideoStream.skip(rangeStart);

            long contentLength = rangeEnd - rangeStart + 1;
            byte[] buffer = new byte[(int) contentLength];
            int contentRead = rangedVideoStream.read(buffer, 0, buffer.length);
            LOGGER.info("Range: "+rangeStart+" - "+rangeEnd);
            LOGGER.info("Read number of bytes = " + contentRead);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range","bytes "+rangeStart+"-"+rangeEnd+"/"+fileLength);
            headers.add("Cache-Control", "no-cache, no-store");
            headers.setContentLength(contentLength);

            return ResponseEntity
                    .status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentLength(contentLength)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(buffer));
        } catch (IOException error) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(CommonConstants.FAILURE, CommonConstants.VIDEO_NOT_UPLOADED,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), LocalDateTime.now().toString()));
        }
    }
}
