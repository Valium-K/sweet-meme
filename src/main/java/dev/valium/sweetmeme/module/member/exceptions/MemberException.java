package dev.valium.sweetmeme.module.member.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberException extends RuntimeException {
    public MemberException() {
        super();
    }

    public MemberException(String message) {
        super(message);
        log.error(message);
    }
}
