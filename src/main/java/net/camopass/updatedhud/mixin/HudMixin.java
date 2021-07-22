package net.camopass.updatedhud.mixin;

import net.camopass.updatedhud.HudRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InGameHud.class)
public class HudMixin {

    @Shadow
    MinecraftClient client;

    @Overwrite
    protected void renderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines,
                                   int regeneratingHeartIndex, float maxHealth, int lastHealth, int health,
                                   int absorption, boolean blinking) {
        HudRender.Companion.ktRenderHealthBar(matrices, player, x, y, lines, regeneratingHeartIndex, maxHealth, lastHealth, health, absorption, blinking, client.textRenderer);
    }
}
