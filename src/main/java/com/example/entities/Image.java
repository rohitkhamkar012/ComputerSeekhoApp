package com.example.entities;
import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "album_id")
    private Album album;

     @Column(name = "image_title", length  = 255, nullable = false)
    private String imageTitle;

    @Column(name = "image_description", length = 255, nullable = false)
    private String imageDescrption;


    // Getters and Setters
    public int getImageId() {
        return imageId;
    }

    public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public String getImageDescrption() {
		return imageDescrption;
	}

	public void setImageDescrption(String imageDescrption) {
		this.imageDescrption = imageDescrption;
	}

	public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}