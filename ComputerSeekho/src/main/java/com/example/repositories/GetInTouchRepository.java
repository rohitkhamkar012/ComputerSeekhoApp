package com.example.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entities.GetInTouch;



@Repository
public interface GetInTouchRepository extends JpaRepository<GetInTouch,Integer> {    
}