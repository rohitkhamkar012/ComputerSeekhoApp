package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entities.News;

@Service
public interface NewsService {
    public List<News> getAllImages();

    public News saveImage(News image);

    public Optional<News> getImageById(int id);

    public void deleteImage(int id);

    public News getLatestNews();
    // public List<Image> getbyAlbum(int id);
}