package dev.valium.sweetmeme.module.post.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostException extends Exception {
    public PostException() {
        super();
    }

    public PostException(String message) {
        super(message);
        log.error(message);
    }
}
