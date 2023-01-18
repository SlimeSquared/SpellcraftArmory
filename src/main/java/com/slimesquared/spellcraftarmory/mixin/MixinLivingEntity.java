package com.slimesquared.spellcraftarmory.mixin;

import com.slimesquared.spellcraftarmory.capability.PlayerSpellCooldownProvider;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    //index found with FSTORE when view -> view bytecode in LivingEntity
    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"), index = 8)
    public float friction2(float f2) {
        var entity = (LivingEntity) (Object) this;
        for(ItemStack armor : entity.getArmorSlots()) {
            if (armor.getItem() instanceof SpellArmorItem spellArmor) {
                var spell = spellArmor.getSpell(armor);
                if (spell == Spells.SpellList.Slide &&
                        entity instanceof Player player) {
                    AtomicBoolean slipping = new AtomicBoolean(false);
                    //slipping for 3 seconds. Resets after not crouched for 1 sec.
                    if (entity.isCrouching() && entity.isOnGround()) {
                        if (!player.getCooldowns().isOnCooldown(spellArmor)) {
                            player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).ifPresent((timer) -> {
                                //decrements 40x per second
                                timer.setCooldown(spell, 2 * 60);
                                System.out.println("set slip cooldown");
                                slipping.set(true);
                            });
                        }
                        player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).ifPresent((timer) -> {
                            if (timer.getCooldown(spell) > 0) {
                                slipping.set(true);
                                System.out.println("timer: " + timer.getCooldown(spell));
                            }
                        });
                        player.getCooldowns().addCooldown(spellArmor, 20);
                    }
                    else {
                        player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).ifPresent((timer) -> {
                            if (timer.getCooldown(spell) > 0) {
                                timer.setCooldown(spell, 0);
                            }
                        });
                    }

                    f2 = slipping.get() ? 1 : f2;
                }
                if (spell == Spells.SpellList.IceSlip) {
                    f2 = Math.max(.989F, f2);
                }
            }
        }
        return f2;
    }

    @ModifyVariable(method = "travel(Lnet/minecraft/world/phys/Vec3;)V", at = @At("STORE"), index = 9)
    public float friction3(float f3) {
        var entity = (LivingEntity) (Object) this;
        for(ItemStack armor : entity.getArmorSlots()) {
            if (armor.getItem() instanceof SpellArmorItem spellArmor) {
                var spell = spellArmor.getSpell(armor);
                if (spell == Spells.SpellList.Slide && entity instanceof Player player && entity.isCrouching() && entity.isOnGround()) {
                    //slips while capability > 0
                    AtomicBoolean slipping = new AtomicBoolean(false);
                    player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).ifPresent((timer) -> {
                        if (timer.getCooldown(spell) > 0) {
                            slipping.set(true);
                            System.out.println("f3 timer: " + timer.getCooldown(spell));
                        }
                    });
                    f3 = slipping.get() ? 1 : f3;
                }
            }
        }
        return f3;
    }
}