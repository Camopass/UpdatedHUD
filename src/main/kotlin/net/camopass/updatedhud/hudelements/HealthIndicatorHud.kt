package net.camopass.updatedhud.hudelements

import net.camopass.updatedhud.HudItem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult

class HealthIndicatorHud: HudItem(0.8F, 0.02F, 100.0F, 30.0F) {
    override fun render(matrices: MatrixStack) {
        val client = MinecraftClient.getInstance()
        val textRenderer = client.textRenderer
        val entity = client.targetedEntity
        if (entity != null && entity is LivingEntity) {
            DrawableHelper.fill(matrices, this.x, this.y, this.x2, this.y2, (0x66000000).toInt())
            textRenderer.draw(matrices, entity.displayName, (this.x + 3).toFloat(), (this.y + 2).toFloat(), 0xFFFFFFFF.toInt())
            val x = getXpercentage(0.81F)
            val y = getYpercentage(0.055F)
            val healthPercentage = (entity.health * 90) / entity.maxHealth
            var color: Long = 0xFF00FF00
            if (entity is Monster) {
                color = 0xFFFF0000
            } else if (entity is EnderDragonEntity) {
                color = 0xFF7303FC
            }
            DrawableHelper.fill(matrices, x, y, (x + 90), (y + 3), 0xAA000000.toInt())
            DrawableHelper.fill(matrices, x, y, (x + healthPercentage).toInt(), (y + 3), color.toInt())
            textRenderer.draw(matrices, entity.health.toInt().toString(), x.toFloat(), y - 10F, 0xFFFFFFFF.toInt())
            val maxHealth = entity.maxHealth.toInt().toString()
            textRenderer.draw(matrices, maxHealth, x + (80F - textRenderer.getWidth(maxHealth) * 0.5F), y - 10F, 0xFFFFFFFF.toInt())
        }
    }

    fun getEntityInCrosshair(): LivingEntity? {
        val client = MinecraftClient.getInstance()
        val hit = client.crosshairTarget

        val entityHit: EntityHitResult? = when(hit?.type) {
            HitResult.Type.BLOCK -> null
            HitResult.Type.ENTITY -> hit as EntityHitResult
            HitResult.Type.MISS -> null
            else -> null
        }
        return if (entityHit != null) {
            if (entityHit is LivingEntity) {
                entityHit.entity as LivingEntity
            } else {
                null
            }
        } else {
            null
        }
    }

}