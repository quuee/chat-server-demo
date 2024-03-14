package cn.quuee.chatServer.model;

import lombok.Data;

@Data
public class SoundInfo {
    /// URL address
    private String sourceUrl;

    /// Local address
    private String soundLocalPath;

    /// Voice file size
    private Double dataSize;

    /// time
    private Integer duration;
}
