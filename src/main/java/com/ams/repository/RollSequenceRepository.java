package com.ams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ams.model.RollSequence;

@Repository
public interface RollSequenceRepository extends JpaRepository<RollSequence, Integer>{

}
