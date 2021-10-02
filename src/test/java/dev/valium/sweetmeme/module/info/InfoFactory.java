package dev.valium.sweetmeme.module.info;


public class InfoFactory {
    public static Info create(String head, String description) {
        return Info.createInfo(null, head, description);
    }
}
