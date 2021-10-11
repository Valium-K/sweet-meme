package dev.valium.sweetmeme.infra.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String context;
}
