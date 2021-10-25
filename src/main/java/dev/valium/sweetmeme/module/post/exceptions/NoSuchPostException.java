package dev.valium.sweetmeme.module.post.exceptions;

public class NoSuchPostException extends PostException {
    public NoSuchPostException() {
        super();
    }

    public NoSuchPostException(String message) {
        super(message);
    }

    public NoSuchPostException(Long id) {
        super(id + "에 해당하는 Post를 찾을 수 없습니다.");
        System.out.println("sssssssssssssssssssss");
    }
}
