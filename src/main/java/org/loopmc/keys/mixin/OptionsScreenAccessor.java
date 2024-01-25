package org.loopmc.keys.mixin;

import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionsScreen.class)
public interface OptionsScreenAccessor {
    @Accessor
    GameOptions getOptions();
}
