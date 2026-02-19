package com.example.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entities.Image;

@Service
public interface ImageService {
    public List<Image> getAllImages();
    public Image saveImage(Image image);
    public Optional<Image> getImageById(int id);
    public void deleteImage(int id);
    // public List<Image> getbyAlbum(int id);
}