package com.pokecafe.cobblealts;

import com.kingpixel.cobbleutils.util.AdventureTranslator;
import com.kingpixel.cobbleutils.util.LuckPermsUtil;
import com.kingpixel.cobbleutils.util.PlayerUtils;
import com.pokecafe.cobblealts.Commands.CommandTree;
import com.pokecafe.cobblealts.Config.Config;
import com.pokecafe.cobblealts.Config.Lang;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.BannedIpEntry;
import net.minecraft.server.BannedIpList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public final class Cobblealts {
    public static final String MOD_ID = "cobblealts";
    public static final String MOD_NAME = "CobbleAlts";
    public static final String PATH = "/config/cobblealts";
    public static Config config = new Config();
    public static Lang lang = new Lang();
    public static Map<String, Set<String>> playersips = new HashMap<>();
    public static MinecraftServer server;

    public static void load() {
        config.init();
        lang.init();
    }

    public static void init() {
        LifecycleEvent.SERVER_STARTED.register(server -> load());
        LifecycleEvent.SERVER_LEVEL_LOAD.register(level -> server = level.getServer());

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> CommandTree.register(dispatcher, registry));

        PlayerEvent.PLAYER_JOIN.register(player -> handlePlayerJoin(player));
    }

    private static void handlePlayerJoin(ServerPlayerEntity player) {
        if (LuckPermsUtil.checkPermission(player, "cobblealts.bypass")) return;

        String ip = player.getIp();
        String playerName = player.getGameProfile().getName();

        player.

        playersips.computeIfAbsent(ip, k -> new HashSet<>()).add(playerName);
        Set<String> names = playersips.get(ip);
        int num = names.size();

        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayersByIp(ip);
        int otherNum = players.size();

        if (otherNum > num) {
            names.clear();
            players.forEach(p -> names.add(p.getGameProfile().getName()));
            num = otherNum;
        }

        if (config.getBlacklistIps().contains(ip)) {
            disconnectPlayer(player, lang.getMessageBlackListIp(), ip, playerName, num);
            return;
        }

        if (num >= config.getNumMax()) {
            notifyAdmins(playerName, ip, num, names);
            banIp(ip, playerName, num, names);
            disconnectPlayer(player, lang.getMessageKick(), ip, playerName, num);
        }
    }

    private static void disconnectPlayer(ServerPlayerEntity player, String messageTemplate, String ip, String playerName, int num) {
        player.networkHandler.disconnect(
                AdventureTranslator.toNative(
                        messageTemplate
                                .replace("%playername%", playerName)
                                .replace("%ip%", ip)
                                .replace("%num%", String.valueOf(num))
                                .replace("%nummax%", String.valueOf(config.getNumMax()))
                                .replace("%names%", String.join(", ", playersips.get(ip)))
                )
        );
    }

    private static void notifyAdmins(String playerName, String ip, int num, Set<String> names) {
        for (ServerPlayerEntity admin : server.getPlayerManager().getPlayerList()) {
            if (LuckPermsUtil.checkPermission(admin, "cobblealts.notify")) {
                PlayerUtils.sendMessage(admin,
                        lang.getMessageNotify()
                                .replace("%playername%", playerName)
                                .replace("%ip%", ip)
                                .replace("%num%", String.valueOf(num))
                                .replace("%nummax%", String.valueOf(config.getNumMax()))
                                .replace("%names%", String.join(", ", names)),
                        lang.getPrefix());
            }
        }
    }

    private static void banIp(String ip, String playerName, int num, Set<String> names) {
        BannedIpList bannedIpList = server.getPlayerManager().getIpBanList();
        BannedIpEntry ipBanEntry = new BannedIpEntry(ip, new Date(), "Cobblealts", new Date(Long.MAX_VALUE), lang.getBanReason());
        bannedIpList.add(ipBanEntry);
    }
}