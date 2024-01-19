package com.nick.propws.util;

import com.nick.propws.dto.MemberDto;
import com.nick.propws.entity.Member;

public class DtoMapper {


    public static MemberDto mapMember(Member m) {
        MemberDto mDto = new MemberDto();
        mDto.setScore(m.getScore());
        return mDto;
    }
}
