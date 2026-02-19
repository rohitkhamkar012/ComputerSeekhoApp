package com.example.repositories;
import org.springframework.stereotype.Repository;

import com.example.entities.Receipt;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ReceiptRepositories extends JpaRepository<Receipt, Integer> {
    
}