package org.loopmc.keys;

import net.ornithemc.osl.entrypoints.api.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoopKeys implements ClientModInitializer {
    public static final String MOD_ID = "keys";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void initClient() {
        LOGGER.info("Loop Keys loaded!");
    }
}
