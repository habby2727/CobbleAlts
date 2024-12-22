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
public class Lang {
    private static String fileName = "lang.json";
    private String prefix;
    private String messageReload;
    private String messageNotify;
    private String messageKick;
    private String messageBlackListIp;
    private String banReason;
    private String messageCheck;
    private String messageDelete;

    public Lang(){
        prefix = "[" + Cobblealts.MOD_NAME + "] ";
        messageReload = "%prefix% Se ha recargado el plugin";
        messageNotify = "%prefix% El jugador %playername% entro con un maximo de cuentas de %num% con la ip: %ip% ";
        messageKick = "%prefix% El jugador %playername% fue expulsado por tener mas de %num% cuentas con la ip: %ip% ";
        messageBlackListIp = "%prefix% La ip %ip% esta en la lista negra";
        messageCheck = "%prefix% Los jugadores %playernames% tiene %num% cuentas con la ip: %ip% ";
        banReason = "Exceso de Multicuentas papu";
        messageDelete = "%prefix% Se han eliminado %num% cuentas de %playername% ";
    }




    public void init() {
        CompletableFuture<Boolean> futureRead = Utils.readFileAsync(Cobblealts.PATH, fileName,
                el -> {
                    Gson gson = Utils.newGson();
                    Lang lang = gson.fromJson(el, Lang.class);
                    prefix = lang.getPrefix();
                    messageNotify = lang.getMessageNotify();
                    messageKick = lang.getMessageKick();
                    banReason = lang.getBanReason();
                    messageBlackListIp = lang.getMessageBlackListIp();
                    messageCheck = lang.getMessageCheck();
                    messageReload = lang.getMessageReload();
                    messageDelete = lang.getMessageDelete();
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
