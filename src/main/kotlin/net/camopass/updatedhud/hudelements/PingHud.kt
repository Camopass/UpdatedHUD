package net.camopass.updatedhud.hudelements

import net.camopass.updatedhud.HudItem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack

class PingHud: HudItem(0.02F, 0.1F, 45.0F, 15.0F) {
    override fun render(matrices: MatrixStack) {
        DrawableHelper.fill(matrices, x, y, x2, y2, (0x66000000).toInt())
        val ping = client.player?.networkHandler?.getPlayerListEntry(client.player?.uuid)?.latency
        textRenderer.draw(matrices, "$ping ms", (x + 3).toFloat(), (y + 4).toFloat(), (0xFFFFFFFF).toInt())
    }
}