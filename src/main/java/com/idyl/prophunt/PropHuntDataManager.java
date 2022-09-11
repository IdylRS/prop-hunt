package com.idyl.prophunt;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Singleton
public class PropHuntDataManager {
    private final String baseUrl = "http://props.idyl.live:8080";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    private PropHuntPlugin plugin;

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private Gson gson;

    public void getAllPropHunters() {
        log.debug("Getting prop hunters...");
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
                        log.info("Successfully fetch prop hunter data");
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

    public void getPropHuntersByUsernames(String[] players) {
        log.info("Getting prop hunters by username...");
        String playersString = String.join(",", players);

        try {
            Request r = new Request.Builder()
                    .url(baseUrl.concat("/prop-hunters/".concat(playersString)))
                    .get()
                    .build();

            okHttpClient.newCall(r).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    log.info("Error getting prop hunt data by username", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        log.info("Successfully fetch prop hunter data");
                        try
                        {
                            JsonArray j = new Gson().fromJson(response.body().string(), JsonArray.class);
                            log.info(j.toString());
                            plugin.updatePlayerData(parsePropHuntData(j));
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

    private HashMap<String, PropHuntPlayerData> parsePropHuntData(JsonArray j) {
        HashMap<String, PropHuntPlayerData> l = new HashMap<>();
        for (JsonElement jsonElement : j)
        {
            JsonObject jObj = jsonElement.getAsJsonObject();
            String username = jObj.get("username").getAsString();
            PropHuntPlayerData d = new PropHuntPlayerData(jObj.get("username").getAsString(),
                    jObj.get("hiding").getAsBoolean(), jObj.get("modelId").getAsInt());
            l.put(username, d);
        }
        return l;
    }
}
