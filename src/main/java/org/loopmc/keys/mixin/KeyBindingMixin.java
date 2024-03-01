package org.loopmc.keys.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.KeyBinding;
import org.loopmc.keys.ResettingKeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin implements ResettingKeyBinding {

	@Shadow
	public static void resetMapping() {
		throw new UnsupportedOperationException("Mixin Injection Failure!");
	}

	@Shadow public int keyCode;
	@Unique
	private int defaultKeyCode;
	@Inject(method = "<init>", at = @At("TAIL"))
	private void storeDefaultKeyCode(String name, int code, CallbackInfo ci){
		defaultKeyCode = code;
	}


	@Override
	public void reset() {
		keyCode = defaultKeyCode;
		Minecraft.getInstance().options.save();
		resetMapping();
	}
}
