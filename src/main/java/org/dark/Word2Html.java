package org.dark;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import retrofit2.Call;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
@Slf4j
public class Word2Html {


    private OkHttpClient okHttpClient;

    private WordConverterClient wordConverterClient;

    public Word2Html(OkHttpClient okHttpClient, WordConverterClient wordConverterClient) {
        this.okHttpClient = okHttpClient;
        this.wordConverterClient = wordConverterClient;
    }

    private UploadResult postFile(String url, File file) throws IOException {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RequestBody targetFormat =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "html");
        RequestBody code =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "86000");

        RequestBody fileLocation =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "local");
        Call<UploadResult> call = wordConverterClient.upload(targetFormat, code, fileLocation, body);
        return call.execute().body();
    }


    public void process(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        log.info("request url: {}, filePath:{}", Constant.POST_URL, filePath);
        UploadResult uploadResult = this.postFile(Constant.POST_URL, path.toFile());
        if (uploadResult == null) {
            throw new RuntimeException("上传文件失败");
        }
        if (!"SUCCESS".equals(uploadResult.getState())) {
            throw new RuntimeException("上传文件失败");
        }
        log.info("uploadResult:{}", uploadResult);
        this.download(Constant.DOWNLOAD_URL, uploadResult.getFilename(), path.getFileName().toString());
    }

    private void download(String url, String filename, String saveFileName) throws IOException {
        log.info("download url :{}", url + filename);
        Request request = new Request.Builder()
                .url(url + filename)
                .build();
        ResponseBody body = okHttpClient.newCall(request).execute().body();
        Files.write(Paths.get(Constant.SAVE_PATH + StringUtil.getFileName(saveFileName) +Constant.HTML), body.bytes());
    }
}
