package com.slimesquared.spellcraftarmory.item;

import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.runecapability.PlayerRuneProvider;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ChalkItem extends ItemNameBlockItem {
    public ChalkItem(Properties properties) {
        super(ModBlocks.RUNE.get(), properties);
    }

    //Same as BlockItem.place, except does not decrement stack. updateBlockStateFromTag and updateState originally private.
    @Override
    public @NotNull InteractionResult place(BlockPlaceContext ctx) {
        if (!ctx.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(ctx);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {

                Player player = blockplacecontext.getPlayer();

                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                }

                if (!this.placeBlock(blockplacecontext, blockstate)) {
                    //above sets block to new one
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);

                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, ctx.getPlayer());
                    level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, Objects.requireNonNull(ctx.getPlayer())), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                    if (player != null && player.level.getBlockEntity(ctx.getClickedPos()) instanceof RuneBlockEntity rune
                            && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpellArmorItem spellArmor
                            && spellArmor.getRuneSpell(player.getItemBySlot(EquipmentSlot.LEGS)) != RuneSpells.None) {
                        rune.setOwnerUUID(player.getUUID());
                        rune.setRuneSpell(spellArmor.getRuneSpell(player.getItemBySlot(EquipmentSlot.LEGS)));
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack stack, BlockState state) {
        BlockState blockstate = state;
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag != null) {
            CompoundTag compoundtag1 = compoundtag.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = state.getBlock().getStateDefinition();

            for(String s : compoundtag1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    String s1 = Objects.requireNonNull(compoundtag1.get(s)).getAsString();
                    blockstate = updateState(blockstate, property, s1);
                }
            }
        }

        if (blockstate != state) {
            level.setBlock(pos, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState updateState(BlockState state, Property<T> p_40595_, String p_40596_) {
        return p_40595_.getValue(p_40596_).map((p_40592_) -> state.setValue(p_40595_, p_40592_)).orElse(state);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        Block block = this.getBlock();

        if (player != null && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpellArmorItem spellArmor && spellArmor.getRuneSpell(player.getItemBySlot(EquipmentSlot.LEGS)) != RuneSpells.None) {
            switch (spellArmor.getRuneSpell(player.getItemBySlot(EquipmentSlot.LEGS))) {
                case CreeperSound -> block = ModBlocks.CREEPER_RUNE.get();
                case Explode -> block = ModBlocks.EXPLODE_RUNE.get();
                case Silence -> block = ModBlocks.SILENCE_RUNE.get();
                case Teleport -> block = ModBlocks.TELEPORT_RUNE.get();
            }
            player.getCapability(PlayerRuneProvider.PLAYER_RUNES).ifPresent(
                    (runes) -> {
                        runes.addRunes(spellArmor.getRuneSpell(player.getItemBySlot(EquipmentSlot.LEGS)), ctx.getClickedPos(), player);
                    }
            );
        }
        BlockState blockstate = block.getStateForPlacement(ctx);
        return blockstate != null && this.canPlace(ctx, blockstate) ? blockstate : null;
    }
}
