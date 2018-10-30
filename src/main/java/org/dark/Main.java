package org.dark;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xiaozefeng
 * @date 2018/10/29 下午9:52
 */
@Slf4j
public class Main {


    private static OkHttpClient okHttpClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constant.POST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        Retrofit retrofit = retrofit();
        OkHttpClient okHttpClient = okHttpClient();
        WordConverterClient wordConverterClient = retrofit.create(WordConverterClient.class);
        File dir = new File(Constant.DIR);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            log.info("文件目录为空");
            return;
        }
        List<File> fileList = Arrays.stream(files)
                .filter(f -> StringUtil.hasSuffix(f.getName(), "doc") || StringUtil.hasSuffix(f.getName(), "docx"))
                .collect(Collectors.toList());

        if (fileList.size() == 0) {
            log.info("文件目录为空");
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(fileList.size());
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            exec.submit(() -> {
                try {
                    Word2Html wh = new Word2Html(okHttpClient, wordConverterClient);
                    wh.process(Constant.DIR + file.getName());
                } catch (IOException e) {
                    log.info(e.getMessage(), e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        exec.shutdown();
    }


}
