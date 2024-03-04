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
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Inject
    private PropHuntPlugin plugin;

    @Inject
    private PropHuntConfig config;

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private Gson gson;

    protected void updatePropHuntApi(PropHuntPlayerData data)
    {
        String username = urlifyString(data.username);
        String url = config.apiServerUrl().concat("/prop-hunters/"+username);

        try
        {
            Request r = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSON, gson.toJson(data)))
                    .build();

            okHttpClient.newCall(r).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    log.debug("Error sending post data", e);
                }

                @Override
                public void onResponse(Call call, Response response)
                {
                    if (response.isSuccessful())
                    {
                        log.debug("Successfully sent prop hunt data");
                        response.close();
                    }
                    else
                    {
                        log.debug("Post request unsuccessful");
                        response.close();
                    }
                }
            });
        }
        catch (IllegalArgumentException e)
        {
            log.error("Bad URL given: " + e.getLocalizedMessage());
        }
    }

    public void getPropHuntersByUsernames(String[] players) {
        String playersString = urlifyString(String.join(",", players));

        try {
            Request r = new Request.Builder()
                    .url(config.apiServerUrl().concat("/prop-hunters/".concat(playersString)))
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
                        try
                        {
                            JsonArray j = gson.fromJson(response.body().string(), JsonArray.class);
                            HashMap<String, PropHuntPlayerData> playerData = parsePropHuntData(j);
                            plugin.updatePlayerData(playerData);
                        }
                        catch (IOException | JsonSyntaxException e)
                        {
                            log.error(e.getMessage());
                        }
                    }

                    response.close();
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
                    jObj.get("hiding").getAsBoolean(), jObj.get("modelID").getAsInt(), jObj.get("orientation").getAsInt());
            l.put(username, d);
        }
        return l;
    }

    private String urlifyString(String str) {
        return str.trim().replaceAll("\\s", "%20");
    }
}
