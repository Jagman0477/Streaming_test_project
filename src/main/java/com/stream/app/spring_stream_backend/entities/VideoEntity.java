package com.stream.app.spring_stream_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VIDEOS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VideoEntity {

    @Id
    @Column(name = "VIDEO_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;
    @Column(name = "VIDEO_TITLE")
    private String videoTitle;
    @Column(name = "VIDEO_DESCRIPTION")
    private String videoDescription;
    @Column(name = "CONTENT_TYPE")
    private String contentType;
    @Column(name = "FILE_PATH")
    private String filePath;

}
