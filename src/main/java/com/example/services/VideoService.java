package com.example.services;

import java.util.List;
import com.example.entities.Video;

public interface VideoService {
    Video addVideo(Video v);
    List<Video> getAllVideos();
    void delete(int videoId);
    Boolean activateVideo(int videoId,Boolean videoIsActive);
}
