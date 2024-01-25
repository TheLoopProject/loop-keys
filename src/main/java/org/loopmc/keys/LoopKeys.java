package org.loopmc.keys;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class LoopKeys implements ClientModInitializer {
    public static final String MOD_ID = "keys";

    @Override
    public void initClient() {
        System.out.println("Loop Keys loaded!");
    }
}
