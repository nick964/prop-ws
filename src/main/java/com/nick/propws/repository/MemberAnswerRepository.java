package com.nick.propws.repository;

import com.nick.propws.entity.Group;
import com.nick.propws.entity.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {

    @Query("update member_answers ma set ma.score = 1 where ma.question.id = :questionId and ma.answer = " +
            "(select answer from master_answers where id = :masterAnswerId)")
    void updateScoresAfterSubmission(int questionId, int masterAnswerId);
}
