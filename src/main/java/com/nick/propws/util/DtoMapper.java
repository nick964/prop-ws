package com.nick.propws.util;

import com.nick.propws.dto.GroupDto;
import com.nick.propws.dto.MemberDto;
import com.nick.propws.dto.UserDto;
import com.nick.propws.entity.Group;
import com.nick.propws.entity.Member;
import com.nick.propws.entity.MemberAnswer;
import com.nick.propws.entity.User;

public class DtoMapper {


    public static MemberDto mapMember(Member m, boolean mapGroup) {
        MemberDto mDto = new MemberDto();
        mDto.setName(m.getUser().getName());
        mDto.setIcon(m.getUser().getIcon());
        mDto.setScore(getMemberScore(m));
        mDto.setGroupAdmin(m.isGroupAdmin());
        mDto.setSubmission_status(m.getSubmission_status() == null || m.getSubmission_status() == 0 ? 0L : 1L);
        if(mapGroup) {
            mDto.setGroupDto(mapFromGroup(m.getGroup()));
        }
        return mDto;
    }



    private static Long getMemberScore(Member m) {
        long score = 0L;
        for(MemberAnswer answer : m.getAnswers()) {
            if(answer.getScore() != null) {
                score += answer.getScore();
            }
        }
        return score;
    }

    public static GroupDto mapFromGroup(Group g) {
        GroupDto groupDto = new GroupDto();
        groupDto.setGroupKey(g.getGroupKey());
        groupDto.setName(g.getName());
        groupDto.setIcon(g.getIcon());
        groupDto.setId(g.getId());
        groupDto.setMemberCount(g.getMembers().size());
        return groupDto;
    }

    public static UserDto mapFromUser(User u) {
        UserDto userDto = new UserDto();
        userDto.setId(u.getId());
        userDto.setName(u.getName());
        userDto.setUsername(u.getUsername());
        userDto.setEmail(u.getEmail());
        return userDto;
    }

}
