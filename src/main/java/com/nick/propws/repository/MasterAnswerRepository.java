package com.nick.propws.repository;

import com.nick.propws.entity.Group;
import com.nick.propws.entity.MasterAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MasterAnswerRepository extends JpaRepository<MasterAnswer, Long> {



}
