package com.slimesquared.spellcraftarmory.mixin;

import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.util.Spells;
import com.slimesquared.spellcraftarmory.util.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"), ordinal = 0)
    public float friction(float f2) {
        var entity = (LivingEntity) (Object) this;
        for(ItemStack armor : entity.getArmorSlots()) {
            if (armor.getItem() instanceof SpellArmorItem spellArmor) {
                var spell = spellArmor.getSpell(armor);
                if (spell == Spells.SpellList.Slide && entity.isCrouching()) {
                    //use 2.5 for addition to make graph intercept (0,1) (makes math simpler)
                    double scaleFactor = 0.6;
                    f2 = (float) Math.max(Math.min(1 / (entity.getDeltaMovement().length() / scaleFactor + 2) + 0.6, 1), f2);
                }
                if (spell == Spells.SpellList.IceSlip) {
                    f2 = Math.max(.989F, f2);
                }
            }
        }
        return f2;
    }
}