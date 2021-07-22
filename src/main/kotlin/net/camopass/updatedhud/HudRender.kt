package net.camopass.updatedhud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import org.jetbrains.annotations.NotNull

abstract class HudRender {

    companion object {

        fun ktRenderHealthBar (
            matrices: MatrixStack, player: PlayerEntity, x: Int, y: Int, lines: Int,
            regeneratingHeartIndex: Int, maxHealth: Float, lastHealth: Int, health: Int,
            absorption: Int, blinking: Boolean, textRenderer: TextRenderer
        ) {
            val maxLen = 80
            // health / maxHealth = x / maxLen
            val healthPercent: Int = ((player.health * maxLen) / maxHealth).toInt()
            DrawableHelper.fill(matrices, x, y, x + maxLen, y + 9, (0x66000000).toInt())
            var col = 0xFF00FF00
            if (player.isFreezing) {
                col = 0xFF43b5cc
            } else if (player.hasStatusEffect(StatusEffects.WITHER)) {
                col = 0xFF323536
            } else if (player.hasStatusEffect(StatusEffects.POISON)) {
                col = 0xFF18780b
            } else if (player.isOnFire) {
                col = 0xFFeb6f1c
            }
            DrawableHelper.fill(matrices, x, y, x + healthPercent, y + 9, (if (blinking) 0xFFAAFFAA else col).toInt())
            // val textRenderer = MinecraftClient.getInstance().textRenderer
            textRenderer.draw(matrices, player.health.toInt().toString(), (x + 2).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())
            textRenderer.draw(matrices, player.maxHealth.toInt().toString(), (x + 67).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())

            val hungerManager = player.hungerManager
            DrawableHelper.fill(matrices, x + 100, y, x + 180, y + 9, (0x66000000).toInt())
            val hungerPercent = (((20 - hungerManager.foodLevel) * 80) / 20)
            DrawableHelper.fill(matrices, x + 100 + hungerPercent, y, x + 180, y + 9, (0xFFba7229).toInt())
            textRenderer.draw(matrices, "20", (x + 102).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())
            textRenderer.draw(matrices, hungerManager.foodLevel.toString(), (x + 168).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())
        }

    }

}