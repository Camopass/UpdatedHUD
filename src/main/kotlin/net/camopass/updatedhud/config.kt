package net.camopass.updatedhud

import net.minecraft.item.ArmorMaterial
import net.minecraft.item.ArmorMaterials

class config {

    val ArmorMap = mapOf<ArmorMaterial, Int>(
        ArmorMaterials.DIAMOND to (0xFF34ebd5).toInt(),
        ArmorMaterials.CHAIN to (0xFFd5ebe7).toInt(),
        ArmorMaterials.GOLD to (0xFFedea3b).toInt(),
        ArmorMaterials.IRON to (0xFFd5ebe7).toInt(),
        ArmorMaterials.LEATHER to (0xFF422509).toInt(),
        ArmorMaterials.NETHERITE to (0xFF140301).toInt(),
        ArmorMaterials.TURTLE to (0xFF055c0d).toInt()
    )

}