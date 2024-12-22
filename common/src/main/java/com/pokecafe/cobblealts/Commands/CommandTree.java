package com.pokecafe.cobblealts.Commands;

import com.kingpixel.cobbleutils.util.AdventureTranslator;
import com.kingpixel.cobbleutils.util.LuckPermsUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pokecafe.cobblealts.Cobblealts;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.List;

public class CommandTree {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registry) {

        LiteralArgumentBuilder<ServerCommandSource> literal = CommandManager.literal("cobblealts")
                .requires(source -> LuckPermsUtil.checkPermission(source, 4, "cobblealts.admin"));

        dispatcher.register(
                literal.then(
                        CommandManager.literal("reload")
                                .executes(context -> {
                                    Cobblealts.load();
                                    context.getSource().sendMessage(AdventureTranslator.toNative(
                                            Cobblealts.lang.getMessageReload()
                                    ));
                                    return 1;
                                })
                ).then(
                        CommandManager.literal("check")
                                .then(
                                        CommandManager.argument("player", EntityArgumentType.player())
                                                .executes(context -> {
                                                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                                    String ip = player.getIp();
                                                    checkIP(ip, context);
                                                    return 1;
                                                })
                                ).then(
                                        CommandManager.argument("ip", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    List<String> ips = Cobblealts.playersips.keySet().stream().toList();
                                                    ips.forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    String ip = StringArgumentType.getString(context, "ip");
                                                    checkIP(ip, context);
                                                    return 1;
                                                })
                                )
                ).then(
                        // Borrar en el map
                        CommandManager.literal("delete")
                                .then(
                                        CommandManager.argument("player", EntityArgumentType.player())
                                                .executes(context -> {
                                                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                                    // Borrar en el map
                                                    deleteIP(player.getIp(), context);
                                                    return 1;
                                                })
                                ).then(
                                        CommandManager.argument("ip", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    List<String> ips = Cobblealts.playersips.keySet().stream().toList();
                                                    ips.forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    String ip = StringArgumentType.getString(context, "ip");
                                                    // Borrar en el map
                                                    deleteIP(ip, context);
                                                    return 1;
                                                })
                                )
                )
        );
    }

    private static void deleteIP(String ip, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Cobblealts.playersips.remove(ip);
        Cobblealts.playersips.put(ip, new HashSet<>());
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();
        String message = Cobblealts.lang.getMessageDelete()
                .replace("%player%", player.getGameProfile().getName())
                .replace("%ip%", ip)
                .replace("%playernames%", Cobblealts.playersips.get(ip).toString())
                .replace("num", String.valueOf(Cobblealts.playersips.get(ip).size()))
                .replace("%nummax%", String.valueOf(Cobblealts.config.getNumMax()));

        if (player == null){
            context.getSource().sendMessage(AdventureTranslator.toNative(
                    message
            ));
        } else {
            player.sendMessage(AdventureTranslator.toNative(
                    message
            ));
        }

    }

    private static void checkIP(String ip, CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayerOrThrow();


        String message = Cobblealts.lang.getMessageCheck()
                .replace("%player%", player.getGameProfile().getName())
                .replace("%ip%", ip)
                .replace("%playernames%", Cobblealts.playersips.get(ip).toString())
                .replace("num", String.valueOf(Cobblealts.playersips.get(ip).size()))
                .replace("%nummax%", String.valueOf(Cobblealts.config.getNumMax()));


        if (player == null){
            context.getSource().sendMessage(AdventureTranslator.toNative(
                    message
            ));
        } else {
           player.sendMessage(AdventureTranslator.toNative(
                   message
           ));
        }
    }

}
