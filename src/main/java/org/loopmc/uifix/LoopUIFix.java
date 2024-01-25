package org.loopmc.uifix;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;

public class LoopUIFix implements ClientModInitializer {
    public static final String MOD_ID = "uifix";

    @Override
    public void initClient() {
        System.out.println("LoopUIFix loaded!");
    }
}
