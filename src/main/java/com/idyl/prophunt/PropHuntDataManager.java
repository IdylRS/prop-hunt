package com.idyl.prophunt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Slf4j
@Singleton
public class PropHuntDataManager {
    private final String baseUrl = "http://props.idyl.live:8080";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private Gson gson;

    public void getPropHunters() {
        try {
            Request r = new Request.Builder()
                    .url(baseUrl.concat("/prop-hunters"))
                    .get()
                    .build();

            okHttpClient.newCall(r).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.debug("Error getting prop hunt data", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        log.debug("Successfully fetch prop hunter data");
                        try
                        {
                            JsonObject j = new Gson().fromJson(response.body().string(), JsonObject.class);
                            log.debug(j.toString());
                        }
                        catch (IOException | JsonSyntaxException e)
                        {
                            log.error(e.getMessage());
                        }
                    }
                }
            });
        }
        catch(IllegalArgumentException e) {
            log.error("Bad URL given: " + e.getLocalizedMessage());
        }
    }
}
