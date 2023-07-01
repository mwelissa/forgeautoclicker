package com.mel.autoclicker.mixins;

import com.mel.autoclicker.core.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow private int rightClickDelayTimer;

    @Shadow private int leftClickCounter;

    @Shadow protected abstract void clickMouse();

    @Shadow public GameSettings gameSettings;

    @Shadow public MovingObjectPosition objectMouseOver;

    @Shadow public EntityPlayerSP thePlayer;

    @Inject(method = "rightClickMouse", at = @At("TAIL"))
    public void rightClickMouse(CallbackInfo ci) {
        this.rightClickDelayTimer = Config.INSTANCE.getRightClickDelay();
    }

    @Inject(method = "clickMouse", at = @At("TAIL"))
    public void clickMouse(CallbackInfo ci) {
        if (this.leftClickCounter <= 0) this.leftClickCounter = Config.INSTANCE.getLeftClickDelay();
    }
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z"))
    public boolean redirectMethod(KeyBinding keyBinding) {
        if (keyBinding.getKeyDescription().equals("key.attack")) {
            return false;
        }
        return keyBinding.isPressed();
    }
    @Inject(method = "sendClickBlockToController", at = @At(value = "HEAD"))
    private void click(boolean leftClick, CallbackInfo ci) {
        if (this.gameSettings.keyBindAttack.isKeyDown() && this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK && !this.thePlayer.isUsingItem()) {
            this.clickMouse();
        }
    }
}
