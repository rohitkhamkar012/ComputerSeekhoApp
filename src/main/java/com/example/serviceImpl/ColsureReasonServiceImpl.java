package com.example.serviceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.ClosureReason;
import com.example.repositories.ClosureReasonRepository;
import com.example.services.ClosureReasonService;



@Service
public class ColsureReasonServiceImpl implements ClosureReasonService{

    @Autowired
    private ClosureReasonRepository closureReasonRepository;
    
    @Override
    public Optional<ClosureReason> getClosureReasonById(Integer id) {
        return closureReasonRepository.findById(id);
    }

    @Override
    public ClosureReason addClosureReason(ClosureReason closureReason) {
        return closureReasonRepository.save(closureReason);
    }

    @Override
    public List<ClosureReason> getAllClosureReasons() {
        return closureReasonRepository.findAll();
    }

    @Override
    public boolean updateClosureReason(ClosureReason closureReason) {
        Optional<ClosureReason> optional =
            closureReasonRepository.findById(closureReason.getClosureReasonId());

        if (optional.isPresent()) {
            ClosureReason existing = optional.get();
            existing.setClosureReasonDesc(closureReason.getClosureReasonDesc());
            existing.setEnquirerName(closureReason.getEnquirerName());
            closureReasonRepository.save(existing);
            return true;
        }
        return false;
    }


    @Override
    public boolean deleteClosureReason(int id) {
        ClosureReason closureReason = closureReasonRepository.findById(id).get();
        if(closureReason != null) {
            closureReasonRepository.delete(closureReason);
            return true;
        }
        return false;
    }
    
}