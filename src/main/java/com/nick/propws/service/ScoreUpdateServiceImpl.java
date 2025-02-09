package com.nick.propws.service;

import com.nick.propws.exceptions.PropSheetException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
public class ScoreUpdateServiceImpl implements ScoreUpdateService {

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    @Scheduled(fixedRate = 5000) // Runs every 60 seconds (adjust as needed)
    @Transactional
    public void updateMembersScores() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.info("Running updateMembersScores at ScoreUpdateServiceImpl - " + LocalDateTime.now().format(formatter));
        try {
            String sql = "UPDATE member_answers ma " +
                    "SET ma.score = CASE " +
                    "    WHEN ma.answer = (SELECT m.answer FROM master_answers m WHERE m.question_id = ma.question_id) THEN 1 " +
                    "    ELSE 0 " +
                    "END " +
                    "WHERE EXISTS (SELECT 1 FROM master_answers m WHERE m.question_id = ma.question_id)";

            int updatedCount = entityManager.createNativeQuery(sql).executeUpdate();

            String updateMemberSql = "UPDATE members m " +
                    "SET m.score = (" +
                    "    SELECT COALESCE(SUM(ma.score), 0) " +
                    "    FROM member_answers ma " +
                    "    WHERE ma.member_id = m.id" +
                    ")";

            int updatedMemberCount = entityManager.createNativeQuery(updateMemberSql).executeUpdate();
        } catch (Exception e) {
            log.error("Error updating member ScoreUpdateServiceImpl", e);
        }

    }
}
