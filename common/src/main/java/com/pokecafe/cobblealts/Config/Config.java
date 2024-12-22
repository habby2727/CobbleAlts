package com.pokecafe.cobblealts.Config;

import com.kingpixel.cobbleutils.util.Utils;

import com.google.gson.Gson;
import com.kingpixel.cobbleutils.CobbleUtils;
import com.kingpixel.cobbleutils.Model.*;
import com.kingpixel.cobbleutils.Model.options.Pokerus;
import com.kingpixel.cobbleutils.util.Utils;
import com.pokecafe.cobblealts.Cobblealts;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class Config {
    private static String fileName = "config.json";
    private boolean notify;
    private Integer numMax;
    private List<String> blacklistIps;

    public Config(){
        blacklistIps =  new ArrayList<>();
        numMax = 3;
    }


    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Cobblealts.PATH, fileName,
                el -> {
                    Gson gson = Utils.newGson();
                    Config config = gson.fromJson(el, Config.class);
                    blacklistIps = config.getBlacklistIps();
                    numMax = config.getNumMax();
                    String data = gson.toJson(this);
                    CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Cobblealts.PATH, fileName,
                            data);
                    if (!futureWrite.join()) {
                        CobbleUtils.LOGGER.fatal("Could not write config.json file for " + Cobblealts.MOD_NAME + ".");
                    }
                });

        if (!futureRead.join()) {
            CobbleUtils.LOGGER.info("No " + fileName + "file found for" + Cobblealts.MOD_NAME + ". Attempting to generate one.");
            Gson gson = Utils.newGson();
            String data = gson.toJson(this);
            CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(Cobblealts.PATH, fileName,
                    data);

            if (!futureWrite.join()) {
                CobbleUtils.LOGGER.fatal("Could not write config.json file for " + Cobblealts.MOD_NAME + ".");
            }
        }

    }
}
