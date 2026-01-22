package com.example.services;

import java.util.List;

import com.example.entities.Album;

public interface AlbumService {
	Album saveAlbum(Album album);
    List<Album> getAllAlbums();
    Album getAlbumById(int albumId);
    boolean updateAlbum(Album album);
    void deleteAlbum(int albumId);

}
