package net.camopass.updatedhud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack

abstract class HudItem(xPercentage: Float, yPercentage: Float, width: Float, height: Float) {
    val scale: Float = 1.0F

    protected val client = MinecraftClient.getInstance()
    protected val textRenderer = client.textRenderer

    private val windowWidth = client.window.scaledWidth
    private val windowHeight = client.window.scaledHeight
    val x = (windowWidth * xPercentage).toInt()
    val y = (windowHeight * yPercentage).toInt()
    val x2 = ((x + width) / scale).toInt()
    val y2 = ((y + height) / scale).toInt()

    abstract fun render(matrices: MatrixStack)

    fun getXpercentage(xPercentage: Float): Int {
        return (windowWidth * xPercentage).toInt()
    }

    fun getYpercentage(yPercentage: Float): Int {
        return (windowWidth * yPercentage).toInt()
    }

}