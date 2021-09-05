package dev.valium.sweetmeme.domain.enums;

public enum Membership {
    /**
     * NEW: 이메일 인증 안한 멤버
     * USER: 이메일 인증 한 멤버
     * PRO: 기부 혹은 장기 미접속이 아닌채 1000일이 경과한 멤버
     */
    NEW, USER, PRO
}
