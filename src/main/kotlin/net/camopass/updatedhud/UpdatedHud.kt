package net.camopass.updatedhud

import com.google.gson.GsonBuilder
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.camopass.updatedhud.hudelements.ClockHud
import net.camopass.updatedhud.hudelements.FpsHud
import net.camopass.updatedhud.hudelements.HealthIndicatorHud
import net.camopass.updatedhud.hudelements.PingHud
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.loader.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText
import org.lwjgl.glfw.GLFW
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path


// For support join https://discord.gg/v6v4pMv

class UpdatedHud: ClientModInitializer {
    val modid = "updatedhud"
    var fps: Int = 0

    lateinit var healthIndicatorHud: HealthIndicatorHud
    lateinit var fpsHud: FpsHud
    lateinit var pingHud: PingHud
    lateinit var clockHud: ClockHud

    override fun onInitializeClient() {
        println("Initializing Updated HUD.")
        println(getConfigDir())

        val keyBinding: KeyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.$modid.dash",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.$modid.dash"
            )
        )

        /*ClientTickEvents.END_CLIENT_TICK.register(
            ClientTickEvents.EndTick { client: MinecraftClient ->
                while (keyBinding.wasPressed()) run {
                    client.currentScreen?.let { openConfig(it) }
                }
            }
        )*/

        HudRenderCallback.EVENT.register { matrices: MatrixStack, _: Float ->
            renderArmorHud(matrices)
            if (!this::healthIndicatorHud.isInitialized) {
                healthIndicatorHud = HealthIndicatorHud()
                fpsHud = FpsHud()
                pingHud = PingHud()
                clockHud = ClockHud()
            }
            healthIndicatorHud.render(matrices)
            fpsHud.render(matrices)
            pingHud.render(matrices)
            clockHud.render(matrices)
        }

    }

    fun getItemCount(playerEntity: ClientPlayerEntity, item: Item): Int {
        var count = 0
        for (itemStack in playerEntity.inventory.main) {
            if (itemStack.item == item) {
                count += itemStack.count
            }
        }
        return count
    }

    fun renderArmorHud(matrices: MatrixStack) {
        val client = MinecraftClient.getInstance()
        val window = client.window
        val width = window.scaledWidth
        val height = window.scaledHeight
        val equipment = client.player?.armorItems
        val armor = mutableListOf<Item>()
        if (equipment != null) {
            for (itemStack in equipment) {
                armor.add(itemStack.item)
            }
        }
        val widthPercent = 0.016F
        val heightPercent = 0.65F
        val w = (width * widthPercent).toInt()
        val h = (height * heightPercent).toInt()
        DrawableHelper.fill(matrices, w, h, w + (h * 0.12).toInt(), h + (h * 0.51).toInt(), (0x66000000).toInt())
        val itemRenderer = client.itemRenderer
        var i = 1
        val x = (width * 0.02F).toInt()
        val y = (height * 0.9F).toInt()
        val itemStack = client.player?.mainHandStack
        val count = itemStack?.item?.let { getItemCount(client.player!!, it) }
        val it = ItemStack(itemStack?.item)
        if (count != null) {
            it.count = count
        }
        itemRenderer.renderInGuiWithOverrides(it, x, y)
        itemRenderer.renderGuiItemOverlay(client.textRenderer, it, x, y)
        if (equipment != null) {
            for (item in equipment) {
                val offsetY = y - (15 * i) - 1
                itemRenderer.renderInGuiWithOverrides(item, x, offsetY)
                itemRenderer.renderGuiItemOverlay(client.textRenderer, item, x, (y - (15 * i)))
                /*
                DrawableHelper.fill(matrices, (x + 25), (offsetY), (x + 20), (offsetY + 14), (0x66000000).toInt())
                val maxDurability = item.maxDamage
                val durability = item.damage
                val damage = (durability * 14) / maxDurability
                DrawableHelper.fill(matrices, (x + 25), offsetY + damage, (x + 20), offsetY + 14, (0xAA00FF00).toInt())
                 */
                ++i
            }
        }


    }

    fun getConfigDir(): String {
        return FabricLoader.INSTANCE.configDir.resolve("UpdatedHud.json").toString()
    }

    /* fun openConfig(parent: Screen) {
        println("Opening screen.")
        val builder: ConfigBuilder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(TranslatableText("title.$modid.config"))
        builder.setSavingRunnable {
            val writer: FileWriter = FileWriter(getConfigDir())
            GsonBuilder().setPrettyPrinting().create().toJson(builder, writer)
            writer.flush()
            writer.close()
        }
        val screen: Screen = builder.build()
        MinecraftClient.getInstance().openScreen(screen)
    } */


    private fun checkExistence() {
        val dir = getConfigDir()
        if (Files.exists(Path.of(dir))) {
            return
        } else {
            Files.createDirectory(Path.of(dir))
        }
    }

}
