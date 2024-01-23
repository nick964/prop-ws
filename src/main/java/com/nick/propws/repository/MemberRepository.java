package com.nick.propws.repository;

import com.nick.propws.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from members m where m.user.id = :userId and m.group.id = :groupId")
    Member findExistingMemberBeforeAdding(Long userId, Long groupId);

}
