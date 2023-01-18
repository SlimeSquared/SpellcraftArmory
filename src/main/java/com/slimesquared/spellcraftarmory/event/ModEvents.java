package com.slimesquared.spellcraftarmory.event;

import com.slimesquared.spellcraftarmory.SpellcraftArmory;
import com.slimesquared.spellcraftarmory.capability.PlayerSpellCooldownProvider;
import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.entity.ModEntityTypes;
import com.slimesquared.spellcraftarmory.entity.client.GhostHorseModel;
import com.slimesquared.spellcraftarmory.entity.client.GhostHorseRenderer;
import com.slimesquared.spellcraftarmory.entity.client.SpellProjectileModel;
import com.slimesquared.spellcraftarmory.entity.client.SpellProjectileRenderer;
import com.slimesquared.spellcraftarmory.entity.client.armor.SpellArmorRenderer;
import com.slimesquared.spellcraftarmory.entity.custom.GhostHorse;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.particle.LightParticles;
import com.slimesquared.spellcraftarmory.particle.ModParticles;
import com.slimesquared.spellcraftarmory.particle.TeleportParticles;
import com.slimesquared.spellcraftarmory.particle.runeemitter.ExplodeRuneEmitter;
import com.slimesquared.spellcraftarmory.particle.runeemitter.TeleRuneEmitter;
import com.slimesquared.spellcraftarmory.capability.PlayerRuneProvider;
import com.slimesquared.spellcraftarmory.util.Spells;
import com.slimesquared.spellcraftarmory.util.Utils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.Objects;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = SpellcraftArmory.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void finishItem(LivingEntityUseItemEvent.Finish e) {
            for(ItemStack armor : e.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor
                        && spellArmor.getSpell(armor) == Spells.SpellList.HealDrink
                        && e.getItem().getItem() == Items.POTION) {
                    if (PotionUtils.getPotion(e.getItem()) == Potions.WATER) {
                        Utils.applyEffect(e.getEntity(), MobEffects.HEAL, 1, 0, false);
                    }
                    else if (PotionUtils.getPotion(e.getItem()) == Potions.THICK) {
                        Utils.applyEffect(e.getEntity(), MobEffects.HEAL, 1, 1, false);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void attack(LivingAttackEvent e) {
            for(ItemStack armor : e.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor
                        && spellArmor.getSpell(armor) == Spells.SpellList.Bloodlust
                        && (e.getSource().msgId.equals("player") || e.getSource().msgId.equals("mob"))
                        && !(e.getSource() instanceof IndirectEntityDamageSource)) {
                    Utils.applyEffect(e.getEntity(), MobEffects.DAMAGE_BOOST, 30, 0, true);
                }
            }
        }

        @SubscribeEvent
        public static void entityTick(LivingEvent.LivingTickEvent e) {
            var entity = e.getEntity();
            for(ItemStack armor : entity.getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor) {
                    var spell = spellArmor.getSpell(armor);
                    if (spell == Spells.SpellList.Photosynthesis
                            && entity.getHealth() < entity.getMaxHealth()
                            && !entity.level.isThundering()
                            && entity.level.getMaxLocalRawBrightness(entity.blockPosition()) == 15) {
                        if (entity.tickCount % 70 == 0) {
                            entity.heal(1F);
                        }
                        if ((entity.tickCount & 35) == 0) {
                            var pos = entity.position().add(.5 * entity.level.random.nextGaussian(), 1.5 + .5 * entity.level.random.nextGaussian(), .5 * entity.level.random.nextGaussian());
                            entity.level.addParticle(ParticleTypes.COMPOSTER, pos.x, pos.y, pos.z, .03 * entity.level.random.nextGaussian(), .03 * entity.level.random.nextGaussian(), .03 * entity.level.random.nextGaussian());
                        }
                    }
                    if (spell == Spells.SpellList.PassiveJump
                            && entity.tickCount % 80 == 0) {
                        Utils.applyEffect(entity, MobEffects.JUMP, 339, 1, false);
                    }
                    if (spell == Spells.SpellList.Unstoppable) {
                        if (entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                            entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                        }
                        if (entity.hasEffect(MobEffects.DIG_SLOWDOWN)
                                && Objects.requireNonNull(entity.getEffect(MobEffects.DIG_SLOWDOWN)).getAmplifier() > 0) {
                            var effect = entity.getEffect(MobEffects.DIG_SLOWDOWN);
                            entity.removeEffect(MobEffects.DIG_SLOWDOWN);
                            assert effect != null;
                            Utils.applyEffect(entity, MobEffects.DIG_SLOWDOWN, effect.getDuration() / 2, 0, effect.showIcon());
                        }
                    }
                    //for players, instead use livingHurtEvent
                    if (spell == Spells.SpellList.LavaCharm
                            && !entity.isOnFire()
                            && !(entity instanceof Player)
                            && entity.tickCount % 20 == 0) {
                        Utils.applyEffect(entity, MobEffects.FIRE_RESISTANCE, 219, 0, false);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void livingFall(LivingFallEvent e) {
            for(ItemStack armor : e.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor
                        && spellArmor.getSpell(armor) == Spells.SpellList.ModerateFall) {
                    e.setDamageMultiplier(e.getDamageMultiplier() / 5);
                    return;
                }
            }
        }

        @SubscribeEvent
        public static void livingHurt(LivingHurtEvent e) {
            if (e.getSource() == DamageSource.MAGIC && e.getEntity().hasEffect(ModEffects.SILENCE.get())) {
                e.setAmount(e.getAmount() * 0.5f);
            }

            if (e.getSource().isFire()) {
                for(ItemStack armor : e.getEntity().getArmorSlots()) {
                    if (armor.getItem() instanceof SpellArmorItem spellArmor
                            && spellArmor.getSpell(armor) == Spells.SpellList.LavaCharm
                            && e.getEntity() instanceof Player player) {
                        if (!player.getCooldowns().isOnCooldown(spellArmor)) {
                            Utils.applyEffect(player, MobEffects.FIRE_RESISTANCE, 219, 0, false);
                            e.setCanceled(true);
                        }
                        else {
                            //removes cooldown, then sets it. In effect: only usable 20 seconds after last took fire damage
                            player.getCooldowns().removeCooldown(spellArmor);
                        }
                        player.getCooldowns().addCooldown(spellArmor, 400);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> e) {
            if (e.getObject() instanceof Player player) {
                if (!player.getCapability(PlayerRuneProvider.PLAYER_RUNES).isPresent()) {
                    e.addCapability(new ResourceLocation(SpellcraftArmory.MOD_ID, "runeproperties"), new PlayerRuneProvider());
                }
                if (!player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).isPresent()) {
                    e.addCapability(new ResourceLocation(SpellcraftArmory.MOD_ID, "spellcooldownproperties"), new PlayerSpellCooldownProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone e) {
            if (e.isWasDeath()) {
                e.getOriginal().reviveCaps();
                e.getOriginal().getCapability(PlayerRuneProvider.PLAYER_RUNES).ifPresent(oldStore -> {
                    e.getEntity().getCapability(PlayerRuneProvider.PLAYER_RUNES).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
                e.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent e) {
            e.register(PlayerRuneProvider.class);
            e.register(PlayerSpellCooldownProvider.class);
        }

        //decrements 40x per second
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent e) {
            e.player.getCapability(PlayerSpellCooldownProvider.PLAYER_SPELL_COOLDOWNS).ifPresent((cooldowns) -> {
                for (var spell : Spells.SpellList.values()) {
                    var time = cooldowns.getCooldown(spell);
                    if (time != null && time > 0) {
                        cooldowns.setCooldown(spell, time - 1);
                        if (spell == Spells.SpellList.Slide) {
                            System.out.println("slide decremented: " + time);
                        }
                    }
                }
            });
        }
    }

    @Mod.EventBusSubscriber(modid = SpellcraftArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent e) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                EntityRenderers.register(ModEntityTypes.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
                EntityRenderers.register(ModEntityTypes.GHOST_HORSE.get(), GhostHorseRenderer::new);
            }
        }

        @SubscribeEvent
        public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions e) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                e.registerLayerDefinition(SpellProjectileModel.SPELL_PROJECTILE_LAYER, SpellProjectileModel::createLayer);
                e.registerLayerDefinition(GhostHorseModel.GHOST_HORSE_LAYER, GhostHorseModel::createLayer);
            }
        }

        @SubscribeEvent
        public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers e) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                e.registerEntityRenderer(ModEntityTypes.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
            }
        }

        @SubscribeEvent
        public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers e) {
            GeoArmorRenderer.registerArmorRenderer(SpellArmorItem.class, SpellArmorRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = SpellcraftArmory.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void registerParticleFactories(final RegisterParticleProvidersEvent e) {
            e.register(ModParticles.LIGHT_PARTICLES.get(), LightParticles.Provider::new);
            e.register(ModParticles.TELEPORT_PARTICLES.get(), TeleportParticles.Provider::new);

            //emitter provider references are different
            e.register(ModParticles.TELE_RUNE_EMITTER.get(), new TeleRuneEmitter.Provider());
            e.register(ModParticles.EXPLODE_RUNE_EMITTER.get(), new ExplodeRuneEmitter.Provider());
        }

        @SubscribeEvent
        public static void entityAttributesEvent(EntityAttributeCreationEvent e) {
            e.put(ModEntityTypes.GHOST_HORSE.get(), GhostHorse.createAttributes().build());
        }
    }
}
