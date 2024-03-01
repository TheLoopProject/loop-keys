package org.loopmc.keys.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import org.loopmc.keys.gui.ControlsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow @Final private GameOptions options;

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/options/ControlsOptionsScreen;<init>(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/options/GameOptions;)V"
        ),
        method = "buttonClicked(Lnet/minecraft/client/gui/widget/ButtonWidget;)V",
        cancellable = true
    )
    public void buttonClicked(ButtonWidget btn, CallbackInfo ci) {
        this.minecraft.openScreen(new ControlsScreen(this, options));

        ci.cancel();
    }
}
