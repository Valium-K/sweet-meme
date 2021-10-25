package dev.valium.sweetmeme.module.member.exceptions;

public class NoSuchMemberException extends MemberException {
    public NoSuchMemberException() {
        super();
    }

    public NoSuchMemberException(String message) {
        super(message);
    }

    public NoSuchMemberException(Long id) {
        super(id + "에 해당하는 맴버를 찾을 수 없습니다.");
    }


}
