package com.nick.propws.repository;

import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.MemberAnswer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {

    @Modifying
    @Transactional
    @Query("update member_answers ma set ma.score = 1 where ma.question.id = :questionId and ma.answer = " +
            "(select answer from master_answers where id = :masterAnswerId)")
    void updateScoresAfterSubmission(int questionId, int masterAnswerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE members m SET m.score = (SELECT SUM(ma.score) FROM member_answers ma WHERE ma.member_id = m.id)", nativeQuery = true)
    void updateMemberScores();

    List<MemberAnswer> findMemberAnswersByMember(Member m);
}
