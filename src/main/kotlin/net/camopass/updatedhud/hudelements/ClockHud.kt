package net.camopass.updatedhud.hudelements

import net.camopass.updatedhud.HudItem
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import kotlin.math.floor

class ClockHud: HudItem(0.02F, 0.18F, 45F, 15F) {

    override fun render(matrices: MatrixStack) {
        val player = client.player
        if (player != null) {
            if (player.inventory.contains(ItemStack(Items.CLOCK))) {
                DrawableHelper.fill(matrices, x, y, x2, y2, 0x66000000)
                /*var time: Int
                var gametime = player.world.timeOfDay.toInt()
                var daysplayed: Int = 0

                while (gametime >= 24000) {
                    gametime -= 24000
                    daysplayed += 1
                }

                if (gametime >= 18000) {
                    time = gametime - 18000
                } else {
                    time = 6000 + gametime
                }

                val ampm: String
                if (time >= 13000) {
                    time -= 12000
                    ampm = " PM"
                } else {
                    if (time >= 12000) {
                        ampm = " PM"
                    } else {
                        ampm = " AM"
                        if (time <= 999) {
                            time += 12000
                        }
                    }
                }
                val hours = "%02d".format(time / 10)

                var minutes = hours[]*/

                var gametime = player.world.timeOfDay.toInt()
                var daysplayed = 0

                while (gametime >= 24000) {
                    gametime -= 24000
                    daysplayed += 1
                }

                // 11834 | 17:50 | 5:50 PM

                var hours = (gametime / 1000) + 6 // 17

                val minutes = floor(60 * ((gametime.toFloat() - ((hours - 6) * 1000)) / 1000)).toInt()

                if (hours >= 24) {
                    hours -= 24
                } // 17

                val ampm: String

                if (hours == 0) {
                    hours = 12
                    ampm = "AM"
                } else {
                    if (hours > 12) {
                        hours -= 12
                        ampm = "PM"
                    } else {
                        ampm = "AM"
                    }
                }

                textRenderer.draw(matrices, "%d:%02d %s".format(hours, minutes, ampm), (x + 2F), (y + 4F), 0xFFFFFFFF.toInt())
            }
        }
    }

}