package com.example.services;


import java.util.List;
import java.util.Optional;

import com.example.entities.ClosureReason;

public interface ClosureReasonService {
    Optional<ClosureReason> getClosureReasonById(Integer id);
    ClosureReason addClosureReason(ClosureReason closureReason);
    List<ClosureReason> getAllClosureReasons();
    boolean updateClosureReason(ClosureReason closureReason);
    boolean deleteClosureReason(int id);
}