package net.camopass.updatedhud.hudelements

import net.camopass.updatedhud.HudItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

class FpsHud: HudItem(0.02F, 0.02F, 45.0F, 15.0F) {
    override fun render(matrices: MatrixStack) {
        DrawableHelper.fill(matrices, this.x, this.y, this.x2, this.y2, (0x66000000).toInt())
        @Suppress("UnresolvedReference")
        val fps = MinecraftClient.getInstance().fpsDebugString
        val re = Regex("""\d+ fps""").find(fps)
        if (re != null) {
            this.textRenderer.draw(matrices, re.value, (this.x + 3).toFloat(), (this.y + 4).toFloat(), (0xFFFFFFFF).toInt())
        }
    }
}