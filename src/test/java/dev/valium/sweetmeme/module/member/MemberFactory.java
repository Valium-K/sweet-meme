package dev.valium.sweetmeme.module.member;

import dev.valium.sweetmeme.module.info.Info;

public class MemberFactory {

    public static Member create(String nickname, String email, String password, String description) {

        Member member = Member.createMember(nickname, email, password);

        member.setMemberInfo(Info.createInfo(null, nickname, description));

        return member;
    }
}
