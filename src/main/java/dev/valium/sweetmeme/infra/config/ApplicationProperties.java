package dev.valium.sweetmeme.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * email 인증시 host 부를 위한 설정
 */
@Data
@Component
@ConfigurationProperties("app")
public class ApplicationProperties {
    private String host;
}
