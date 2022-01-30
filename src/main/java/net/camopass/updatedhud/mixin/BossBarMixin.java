package net.camopass.updatedhud.mixin;

import net.camopass.updatedhud.HudRender;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BossBarHud.class)
public class BossBarMixin {

    @Overwrite
    private void renderBossBar(MatrixStack matrices, int x, int y, BossBar bossBar) {
        HudRender.Companion.renderBossBar(matrices, x, y, bossBar);
    }

}
