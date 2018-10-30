package org.dark;

import lombok.Data;

/**
 * @author xiaozefeng
 * @date 2018/10/29 下午9:39
 */
@Data
public class UploadResult {

    private String ext;

    private String filename;

    private String server;

    private String state;
}
