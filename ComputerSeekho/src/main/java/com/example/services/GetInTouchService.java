package com.example.services;
import java.util.List;

import com.example.entities.GetInTouch;



public interface GetInTouchService {
    void save(GetInTouch getInTouch);
    List<GetInTouch> getAll();
    void deleteById(int getInTouchId);
}