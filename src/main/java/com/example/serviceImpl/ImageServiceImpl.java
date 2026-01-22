package com.example.serviceImpl;
import java.util.List; // Add this import statement
import java.util.Optional;

//import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Image;
import com.example.repositories.ImageRepository;
import com.example.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    
    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(Image image)
    {
        return imageRepository.save(image);
    }

    public List<Image> getAllImages()
    {
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(int id)
    {
        return imageRepository.findById( id);
    }

    public void deleteImage(int id)
    {
        imageRepository.deleteById(id);
    }

    // @Override
    // public List<Image> getbyAlbum(int id) {
    //     return imageRepository.findByAlbumId(id);
    // }


}
