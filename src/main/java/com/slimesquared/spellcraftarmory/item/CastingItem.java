package com.slimesquared.spellcraftarmory.item;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.runecapability.PlayerRuneProvider;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import com.slimesquared.spellcraftarmory.util.Spells;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class CastingItem extends Item {
    public int spellLvl;

    //spell #: 0=primary (head), 1=secondary (chest), 2=rune wand (num not on leggings), 3=passive
    public CastingItem(Properties properties, int spellLvl) {
        super(properties);
        this.spellLvl = spellLvl;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        //0 for no spell, -1 for disabled spell, 1 for completed spell (play sound in spell class)
        AtomicInteger cast = new AtomicInteger();
        cast.set(0);

        if (player.hasEffect(ModEffects.SILENCE.get())) {
            cast.set(-1);
        }

        if (cast.get() == 0) {
            for(ItemStack armor : player.getArmorSlots()) {
                if (armor.getItem() instanceof SpellArmorItem spellArmor) {
                    if (spellArmor.spellLvl == spellLvl) {
                        player.getCooldowns().addCooldown(this, Spells.castSpell(spellArmor.getSpell(armor), player) );
                        cast.set(1);
                    }
                    if (spellArmor.getRuneSpell(armor) != RuneSpells.None && spellLvl == 2) {
                        player.getCapability(PlayerRuneProvider.PLAYER_RUNES).ifPresent(
                                (runes) -> {
                                    var runePos = runes.getRune(spellArmor.getRuneSpell(armor));
                                    if (runePos != null) {
                                        if (runePos.dim == level.dimension()
                                                && level.getBlockEntity(runePos.pos) instanceof RuneBlockEntity runeEntity
                                                && runeEntity.getOwnerUUID().equals(player.getUUID()) && runeEntity.getRuneSpell() == spellArmor.getRuneSpell(armor)
                                                && runeEntity.getBlockState().getBlock() instanceof GenericRuneBlock runeBlock) {
                                            runeBlock.activate(player, runePos.pos);
                                            player.getCooldowns().addCooldown(this, RuneSpells.getCooldown(spellArmor.getRuneSpell(armor)));
                                            cast.set(1);
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        }

        if (cast.get() == 1) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        else {
            if (level.isClientSide) {
                level.playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.PLAYERS, 1F, 1F, false);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        assert player != null;
        var blockPos = ctx.getClickedPos();
        var block = player.level.getBlockState(blockPos);
        var setBlock = block;

        for(ItemStack armor : player.getArmorSlots()) {
            if (armor.getItem() instanceof SpellArmorItem spellArmor && spellArmor.spellLvl == spellLvl) {
                var spell = spellArmor.getSpell(armor);
                if (spell == Spells.SpellList.Crack) {
                    if (block.getBlock() == Blocks.STONE) {
                        setBlock = Blocks.COBBLESTONE.defaultBlockState();
                    }
                    if (block.getBlock() == Blocks.STONE_SLAB) {
                        setBlock = Blocks.COBBLESTONE_SLAB.withPropertiesOf(block);
                    }
                    if (block.getBlock() == Blocks.STONE_STAIRS) {
                        setBlock = Blocks.COBBLESTONE_STAIRS.withPropertiesOf(block);
                    }

                    if (block.getBlock() == Blocks.STONE_BRICKS) {
                        setBlock = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
                    }

                    if (block.getBlock() == Blocks.DEEPSLATE_BRICKS) {
                        setBlock = Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
                    }
                    if (block.getBlock() == Blocks.DEEPSLATE_TILES) {
                        setBlock = Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState();
                    }

                    if (block.getBlock() == Blocks.NETHER_BRICKS) {
                        setBlock = Blocks.CRACKED_NETHER_BRICKS.defaultBlockState();
                    }

                    if (block.getBlock() == Blocks.POLISHED_BLACKSTONE_BRICKS) {
                        setBlock = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
                    }

                    if (block.getBlock() == Blocks.SMOOTH_SANDSTONE) {
                        setBlock = Blocks.SANDSTONE.defaultBlockState();
                    }
                    if (block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE) {
                        setBlock = Blocks.RED_SANDSTONE.defaultBlockState();
                    }
                    player.level.setBlockAndUpdate(blockPos, setBlock);
                }
                if (spell == Spells.SpellList.Mossify) {
                    if (block.getBlock() == Blocks.COBBLESTONE) {
                        setBlock = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
                    }
                    if (block.getBlock() == Blocks.COBBLESTONE_SLAB) {
                        setBlock = Blocks.MOSSY_COBBLESTONE_SLAB.withPropertiesOf(block);
                    }
                    if (block.getBlock() == Blocks.COBBLESTONE_STAIRS) {
                        setBlock = Blocks.MOSSY_COBBLESTONE_STAIRS.withPropertiesOf(block);
                    }
                    if (block.getBlock() == Blocks.COBBLESTONE_WALL) {
                        setBlock = Blocks.MOSSY_COBBLESTONE_WALL.withPropertiesOf(block);
                    }

                    if (block.getBlock() == Blocks.STONE_BRICKS) {
                        setBlock = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
                    }
                    if (block.getBlock() == Blocks.STONE_BRICK_SLAB) {
                        setBlock = Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(block);
                    }
                    if (block.getBlock() == Blocks.STONE_BRICK_STAIRS) {
                        setBlock = Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(block);
                    }
                    if (block.getBlock() == Blocks.STONE_BRICK_WALL) {
                        setBlock = Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(block);
                    }
                    player.level.setBlockAndUpdate(blockPos, setBlock);
                }
            }
        }
        return super.useOn(ctx);
    }
}
