package com.pokecafe.cobblealts.forge;

import com.pokecafe.cobblealts.Cobblealts;
import net.minecraftforge.fml.common.Mod;

@Mod(Cobblealts.MOD_ID)
public final class CobblealtsForge {
    public CobblealtsForge() {
        Cobblealts.init();
    }
}
