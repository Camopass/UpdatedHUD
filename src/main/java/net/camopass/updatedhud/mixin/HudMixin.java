package net.camopass.updatedhud.mixin;

import net.camopass.updatedhud.HudRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")

@Mixin(InGameHud.class)
public class HudMixin {

    @Shadow
    MinecraftClient client;

    @Shadow
    public final native PlayerEntity getCameraPlayer();

    @Overwrite
    protected void renderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines,
                                   int regeneratingHeartIndex, float maxHealth, int lastHealth, int health,
                                   int absorption, boolean blinking) {
        HudRender.Companion.ktRenderHealthBar(matrices, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking, client.textRenderer);
    }

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V"))
    private void renderArmorOverlay(MatrixStack matrices, CallbackInfo ci) {
        HudRender.Companion.renderArmorBar(matrices, getCameraPlayer(), client.textRenderer);
    }

    @Overwrite
    public void renderExperienceBar(MatrixStack matrices, int x) {
        HudRender.Companion.renderXPBar(matrices, x, client, client.textRenderer);
    }

    @Overwrite
    public void renderMountJumpBar(MatrixStack matrices, int x) {
        HudRender.Companion.renderJumpBar(matrices, x, client, getCameraPlayer());
    }

    @Overwrite
    private void renderMountHealth(MatrixStack matrices) {
        HudRender.Companion.renderMountHealth(matrices, client, getCameraPlayer());
    }

}
