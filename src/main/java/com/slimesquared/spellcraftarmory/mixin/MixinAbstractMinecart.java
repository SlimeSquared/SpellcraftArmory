package com.slimesquared.spellcraftarmory.mixin;

import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractMinecart.class)
public class MixinAbstractMinecart {

    //doubles minecart speed cap. Expected return type and false condition errors can be ignored.
    @ModifyVariable(method = "getMaxSpeedWithRail()D", at = @At("STORE"), ordinal = 0, remap = false)
    public float speed(float railMaxSpeed) {
        if ((Object) this instanceof Minecart minecart
                && !minecart.getPassengers().isEmpty()) {
            for (var entity : minecart.getPassengers()) {
                for (var armor : entity.getArmorSlots()) {
                    if (armor.getItem() instanceof SpellArmorItem spellArmor
                            && spellArmor.getSpell(armor) == Spells.SpellList.MinecartSpeed) {
                        return 2 * railMaxSpeed;
                    }
                }
            }
        }
        return railMaxSpeed;
    }
}
