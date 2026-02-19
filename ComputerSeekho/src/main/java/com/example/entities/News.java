package com.example.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "News_id")
    private int newsId;

    @Column(name = "News_url", length = 255, nullable = false)
    private String newsUrl;

    @Column(name = "News_title", length = 255, nullable = false)
    private String newsTitle;

    @Column(name = "News_description", length = 255, nullable = false)
    private String newsDescription;

}