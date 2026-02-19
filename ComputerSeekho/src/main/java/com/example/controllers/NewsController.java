package com.example.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.ApiResponse;
import com.example.entities.News;
import com.example.serviceImpl.NewsServiceImpl;

@RestController
@RequestMapping("/News")
@CrossOrigin("*")
public class NewsController {

    @Autowired
    private NewsServiceImpl newsService; // Fixed naming

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNews(@RequestBody News news) { // Renamed method for clarity
        newsService.saveImage(news);
        return new ResponseEntity<>(new ApiResponse("Image Uploaded Successfully", LocalDateTime.now()),
                HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllImages() {
        List<News> images = newsService.getAllImages();
        return new ResponseEntity<>(images, HttpStatus.OK); // Fixed status code
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<News> getImageById(@PathVariable Integer id) {
        Optional<News> image = newsService.getImageById(id);
        return image.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable int id) {
        newsService.deleteImage(id);
        return new ResponseEntity<>(new ApiResponse("Image deleted successfully", LocalDateTime.now()), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<News> getLatestNews() {
        News latestNews = newsService.getLatestNews();
        if (latestNews != null) {
            return new ResponseEntity<>(latestNews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}