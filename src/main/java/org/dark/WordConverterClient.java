package org.dark;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author xiaozefeng
 * @date 2018/10/30 10:23 AM
 */
public interface WordConverterClient {

    @Multipart
    @POST("convert/convert-batch-win.php")
    Call<UploadResult> upload(@Part("targetformat") RequestBody targetformat,
                              @Part("code") RequestBody code,
                              @Part("filelocation") RequestBody filelocation,
                              @Part MultipartBody.Part file);
}
