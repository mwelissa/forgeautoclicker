package com.mel.autoclicker.mixins;

import com.mel.autoclicker.AutoClicker;
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

    @Redirect(method = "rightClickMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelayTimer:I"))
    public void cancelBlockDelay(Minecraft instance, int value) {
        if (!Config.INSTANCE.getRightEnabled()) this.rightClickDelayTimer = value;
    }

    @Redirect(method = "clickMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;leftClickCounter:I", opcode = 181))
    public void cancelCreativeDelay(Minecraft instance, int value) {
        if (!Config.INSTANCE.getLeftEnabled()) this.leftClickCounter = value;
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"))
    public void addRightCps(CallbackInfo ci) {
        AutoClicker.ShowCps.INSTANCE.addRight();
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void addLeftCps(CallbackInfo ci) {
        if (leftClickCounter <= 0) AutoClicker.ShowCps.INSTANCE.addLeft();
    }

    @Inject(method = "rightClickMouse", at = @At("TAIL"))
    public void rightClickMouse(CallbackInfo ci) {
        if (rightClickDelayTimer <= 0 && Config.INSTANCE.getRightEnabled()) this.rightClickDelayTimer = Config.INSTANCE.getRightClickDelay();
    }

    @Inject(method = "clickMouse", at = @At("TAIL"))
    public void clickMouse(CallbackInfo ci) {
        if (this.leftClickCounter <= 0 && this.gameSettings.keyBindAttack.isKeyDown() && this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK && Config.INSTANCE.getLeftEnabled()) this.leftClickCounter = Config.INSTANCE.getLeftClickDelay();
    }

    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isPressed()Z"))
    public boolean redirectMethod(KeyBinding keyBinding) {
        if (keyBinding == this.gameSettings.keyBindAttack && this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK && this.gameSettings.keyBindAttack.isKeyDown() && Config.INSTANCE.getLeftEnabled()) {
            return false;
        }
        return keyBinding.isPressed();
    }

    @Inject(method = "sendClickBlockToController", at = @At(value = "TAIL"))
    private void click(boolean leftClick, CallbackInfo ci) {
        if (this.gameSettings.keyBindAttack.isKeyDown() && this.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK && !this.thePlayer.isUsingItem() && Config.INSTANCE.getLeftEnabled()) {
            this.clickMouse();
        }
    }
}
