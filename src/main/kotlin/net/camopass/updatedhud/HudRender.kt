package net.camopass.updatedhud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials

abstract class HudRender {

    companion object {

        fun ktRenderHealthBar (
            matrices: MatrixStack, player: PlayerEntity, x: Int, y: Int, lines: Int,
            regeneratingHeartIndex: Int, preMaxHealth: Float, lastHealth: Int, preHealth: Int,
            absorption: Int, blinking: Boolean, textRenderer: TextRenderer
        ) {
            val maxHealth = preMaxHealth + player.absorptionAmount
            val maxLen = 81
            val health = player.health + player.absorptionAmount
            // health / maxHealth = x / maxLen
            val healthPercent: Int = ((health * maxLen) / maxHealth).toInt()
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
            } else if (player.air < 0) {
                col = 0xFF0b0866
            }
            DrawableHelper.fill(matrices, x, y, x + healthPercent, y + 9, (if (blinking) 0xFFAAFFAA else col).toInt())
            // val textRenderer = MinecraftClient.getInstance().textRenderer
            textRenderer.draw(matrices, health.toInt().toString(), (x + 2).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())
            textRenderer.draw(matrices, maxHealth.toInt().toString(), (x + 67).toFloat(), (y + 1).toFloat(), (0xFFFFFFFF).toInt())

            var yPos: Int = y
            if (player.vehicle != null) {
                if (player.vehicle is LivingEntity) {
                    yPos -= 10
                }
            }

            var hungerColor = 0xFFba7229
            val hungerManager = player.hungerManager

            if (player.hasStatusEffect(StatusEffects.HUNGER)) {
                hungerColor = 0xFF51690f
            }

            DrawableHelper.fill(matrices, x + 100, yPos, x + 181, yPos + 9, (0x66000000).toInt())
            val hungerPercent = (((20 - hungerManager.foodLevel) * 81) / 20)
            DrawableHelper.fill(matrices, x + 100 + hungerPercent, yPos, x + 181, yPos + 9, (hungerColor).toInt())
            textRenderer.draw(matrices, "20", (x + 102).toFloat(), (yPos + 1).toFloat(), (0xFFFFFFFF).toInt())
            textRenderer.draw(matrices, hungerManager.foodLevel.toString(), (x + 168).toFloat(), (yPos + 1).toFloat(), (0xFFFFFFFF).toInt())
        }

        fun renderAirBar(matrices: MatrixStack, player: PlayerEntity, textRenderer: TextRenderer, xPos: Int, yPos: Int) {
            if (player.air != 300) {
                var air = player.air
                if (air < 0) {
                    air = 0
                }
                DrawableHelper.fill(matrices, xPos + 100, yPos, xPos + 181, yPos - 9, (0x66000000).toInt())
                val percentage = (((300 - air) * 82) / 300)
                DrawableHelper.fill(matrices, xPos + 100 + percentage, yPos, xPos + 181, yPos - 9, (0xFF42b3f5).toInt())
                textRenderer.draw(matrices,
                    (air / 3).toString(), (xPos + 102).toFloat(), (yPos - 8).toFloat(), (0xFFFFFFFF).toInt())
            }
        }

        fun renderArmorBar(matrices: MatrixStack, player: PlayerEntity, textRenderer: TextRenderer) {
            val window = MinecraftClient.getInstance().window
            val windowWidth = window.scaledWidth
            val windowHeight = window.scaledHeight
            val xPos = (windowWidth / 2 - 91)
            val yPos = windowHeight - 40
            val equipment = player.armorItems
            val renderEquipmentColor = mutableListOf<Int>()
            val renderEquipmentPercentage = mutableListOf<Int>()
            val ArmorMap = mapOf<ArmorMaterial, Int>(
                ArmorMaterials.DIAMOND to (0xFF34ebd5).toInt(),
                ArmorMaterials.CHAIN to (0xFFbdd1f0).toInt(),
                ArmorMaterials.GOLD to (0xFFedea3b).toInt(),
                ArmorMaterials.IRON to (0xFFd5ebe7).toInt(),
                ArmorMaterials.LEATHER to (0xFF422509).toInt(),
                ArmorMaterials.NETHERITE to (0xFF210b04).toInt(),
                ArmorMaterials.TURTLE to (0xFF055c0d).toInt()
            )
            var totalProtection = 0
            for (itemStack in equipment) {
                if (itemStack.item is ArmorItem) {
                    val armor = itemStack.item as ArmorItem
                    val color = ArmorMap[armor.material]
                    val percentage = armor.protection
                    totalProtection += percentage
                    if (color != null) {
                        renderEquipmentColor.add(color)
                        renderEquipmentPercentage.add(percentage)
                    }
                } else {
                    renderEquipmentColor.add((0x00000000).toInt())
                    renderEquipmentPercentage.add(0)
                }
            }
            if (totalProtection == 0) {
                renderAirBar(matrices, player, textRenderer, xPos, yPos)
                return
            }
            // DrawableHelper.fill(matrices, xPos, yPos, xPos + 81, yPos - 9, (0xFF111111).toInt())
            var tempX = xPos
            for (i in 0..3) {
                val percentage = renderEquipmentPercentage[i]
                val color = renderEquipmentColor[i]
                val renderWidth = ((percentage * 82) / totalProtection)
                DrawableHelper.fill(matrices, tempX, yPos, tempX + renderWidth, yPos - 9, color)
                tempX += renderWidth
                textRenderer.draw(matrices, totalProtection.toString(), (xPos + 67).toFloat(), (yPos - 8).toFloat(), (0xFFFFFFFF).toInt())
            }

            renderAirBar(matrices, player, textRenderer, xPos, yPos)

        }

        fun renderXPBar(matrices: MatrixStack, x: Int, client: MinecraftClient, textRenderer: TextRenderer) {
            val player = client.player
            if (player != null) {
                val level = player.experienceLevel
                val progress = player.experienceProgress
                val windowWidth = client.window.scaledWidth
                val windowHeight = client.window.scaledHeight
                val xPos = (windowWidth / 2 - 91)
                val yPos = windowHeight - 23
                DrawableHelper.fill(matrices, xPos, yPos, xPos + 181, yPos - 6, (0x66000000).toInt())
                DrawableHelper.fill(matrices, xPos, yPos, xPos + (progress * 181).toInt(), yPos - 6, (0xFF44FF11).toInt())
                textRenderer.draw(matrices, level.toString(), (xPos + 85).toFloat(), (yPos - 15).toFloat(), (0xFF44FF11).toInt())
            }
        }

        fun renderJumpBar(matrices: MatrixStack, x: Int, client: MinecraftClient, player: PlayerEntity) {
            val windowWidth = client.window.scaledWidth
            val windowHeight = client.window.scaledHeight
            val xPos = (windowWidth / 2 - 91)
            val yPos = windowHeight - 23
            DrawableHelper.fill(matrices, xPos, yPos, xPos + 181, yPos - 6, (0x66000000).toInt())
            val jumpProgress = client.player?.mountJumpStrength
            if (jumpProgress != null) {
                DrawableHelper.fill(matrices, xPos, yPos, xPos + (jumpProgress * 181).toInt(), yPos - 6, (0xFF44FF22).toInt())
            }
        }

        fun renderMountHealth(matrices: MatrixStack, client: MinecraftClient, player: PlayerEntity) {
            if (player.vehicle == null) {
                return
            }
            val window = client.window
            val windowWidth = window.scaledWidth
            val windowHeight = window.scaledHeight
            val xPos = (windowWidth / 2 + 9)
            val yPos = windowHeight - 30
            DrawableHelper.fill(matrices, xPos, yPos, xPos + 81, yPos - 9, (0x66000000).toInt())
            val vehicle = player.vehicle
            if (vehicle is LivingEntity) {
                val health = vehicle.health
                val maxHealth = vehicle.maxHealth
                val healthPercentage = (((maxHealth - health) * 81) / maxHealth).toInt()
                DrawableHelper.fill(matrices, xPos + healthPercentage, yPos, xPos + 81, yPos - 9, (0xFFAAFF33).toInt())
                val textRenderer = client.textRenderer
                textRenderer.draw(matrices, maxHealth.toInt().toString(), (xPos + 2).toFloat(), (yPos - 8).toFloat(), (0xFFFFFFFF).toInt())
                textRenderer.draw(matrices, health.toInt().toString(), (xPos + 68).toFloat(), (yPos - 8).toFloat(), (0xFFFFFFFF).toInt())
            }
        }

    }

}