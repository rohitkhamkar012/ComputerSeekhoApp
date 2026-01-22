package com.example.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.ClosureReason;
import com.example.repositories.ClosureReasonRepository;

@Service

public class ClosureReasonServiceImpl implements ClosureReasonService {
	
	@Autowired
    private ClosureReasonRepository closureReasonRepository;

	@Override
	public Optional<ClosureReason> getClosureReasonById(Integer id) {
		// TODO Auto-generated method stub
		 return closureReasonRepository.findById(id);
	}

	@Override
	public ClosureReason addClosureReason(ClosureReason closureReason) {
		// TODO Auto-generated method stub
		 return closureReasonRepository.save(closureReason);
	}

	@Override
	public List<ClosureReason> getAllClosureReasons() {
		// TODO Auto-generated method stub
		 return closureReasonRepository.findAll();
	}

	@Override
	public boolean updateClosureReason(ClosureReason closureReason) {
		// TODO Auto-generated method stub
		ClosureReason closureReason2 = closureReasonRepository.findById(closureReason.getClosureReasonId()).get();
        if(closureReason2 != null) {
            closureReason2.setClosureReasonDesc(closureReason.getClosureReasonDesc());
            closureReasonRepository.save(closureReason2);
            return true;
        }
        return false;
	}

	@Override
	public boolean deleteClosureReason(int id) {
		// TODO Auto-generated method stub
		 ClosureReason closureReason = closureReasonRepository.findById(id).get();
	        if(closureReason != null) {
	            closureReasonRepository.delete(closureReason);
	            return true;
	        }
	        return false;
	}
	
	 
}
