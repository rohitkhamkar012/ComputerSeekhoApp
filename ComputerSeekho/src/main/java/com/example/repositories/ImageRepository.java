package com.example.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.Image;



@Repository
public interface ImageRepository extends JpaRepository<Image , Integer>{
    // @Query("select i from Image i where i.album_id = ?1")
    // List<Image> findByAlbumId(int album_id);

}