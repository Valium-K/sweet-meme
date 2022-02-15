package dev.valium.sweetmeme.module.processor;

import dev.valium.sweetmeme.infra.config.FileConfig;

public class EnvProcessor {
    public static String getActiveProfile() {
        return System.getProperty("spring.profiles.active");
    }
}
