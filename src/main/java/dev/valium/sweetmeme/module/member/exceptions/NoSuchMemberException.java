package dev.valium.sweetmeme.module.member.exceptions;

public class NoSuchMemberException extends MemberException {
    public NoSuchMemberException() {
        super();
    }

    public NoSuchMemberException(String nicknameOrEmail) {
        super(nicknameOrEmail + "에 해당하는 멤버를 찾을 수 없습니다.");
    }

    public NoSuchMemberException(Long id) {
        super(id + "에 해당하는 맴버를 찾을 수 없습니다.");
    }
}
