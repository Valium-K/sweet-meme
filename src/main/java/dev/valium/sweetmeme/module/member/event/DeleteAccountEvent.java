package dev.valium.sweetmeme.module.member.event;


import dev.valium.sweetmeme.module.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteAccountEvent {
    private final Member member;
}
