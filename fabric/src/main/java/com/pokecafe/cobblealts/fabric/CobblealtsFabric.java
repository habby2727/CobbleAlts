package com.pokecafe.cobblealts.fabric;

import com.pokecafe.cobblealts.Cobblealts;
import net.fabricmc.api.ModInitializer;

public final class CobblealtsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Cobblealts.init();
    }
}
