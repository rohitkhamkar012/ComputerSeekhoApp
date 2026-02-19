package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    News findTopByOrderByNewsIdDesc();
}