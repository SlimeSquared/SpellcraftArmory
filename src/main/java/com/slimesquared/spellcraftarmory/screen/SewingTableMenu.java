package com.slimesquared.spellcraftarmory.screen;

import com.slimesquared.spellcraftarmory.block.ModBlocks;
import com.slimesquared.spellcraftarmory.item.ModItems;
import com.slimesquared.spellcraftarmory.item.SpellArmorItem;
import com.slimesquared.spellcraftarmory.recipe.SewingTableRecipe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SewingTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;

    final Slot focusSlot;
    final Slot clothSlot;
    final Slot templateSlot;
    private final Slot resultSlot;
    private final Player player;
    long lastSoundTime;
    public int focusCost;
    public int clothCost;
    public SewingTableMenu(int id, Inventory inv) {
        this(id, inv, ContainerLevelAccess.NULL);
    }

    public SewingTableMenu(int id, Inventory inv, ContainerLevelAccess access) {
        super(ModMenuTypes.SEWING_TABLE_MENU.get(), id);
        this.access = access;
        this.player = inv.player;
        //define id, x, y of top left corner of slot from top left of gui (transition from dark to medium gray)
        this.focusSlot = this.addSlot(new Slot(this.inputContainer, 0, 37, 27) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return true;
            }
        });
        this.clothSlot = this.addSlot(new Slot(this.inputContainer, 1, 57, 27) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.SPELL_CLOTH.get();
            }
        });
        this.templateSlot = this.addSlot(new Slot(this.inputContainer, 2, 47, 46) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return (stack.getItem() instanceof SpellArmorItem || stack.getItem() instanceof DyeableArmorItem);
            }
        });
        this.resultSlot = this.addSlot(new Slot(this.outputContainer, 0, 119, 37) {
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            public void onTake(@NotNull Player player, @NotNull ItemStack resultStack) {
                if (SewingTableMenu.this.focusCost > 0) {
                    ItemStack focusStack = SewingTableMenu.this.focusSlot.getItem();
                    if (!focusStack.isEmpty() && focusStack.getCount() > SewingTableMenu.this.focusCost) {
                        focusStack.shrink(SewingTableMenu.this.focusCost);
                        SewingTableMenu.this.focusSlot.set(focusStack);
                    }
                    else {
                        SewingTableMenu.this.focusSlot.set(ItemStack.EMPTY);
                    }
                }
                else {
                    SewingTableMenu.this.focusSlot.set(ItemStack.EMPTY);
                }

                if (SewingTableMenu.this.clothCost > 0) {
                    ItemStack clothStack = SewingTableMenu.this.clothSlot.getItem();
                    if (!clothStack.isEmpty() && clothStack.getCount() > SewingTableMenu.this.clothCost) {
                        clothStack.shrink(SewingTableMenu.this.clothCost);
                        SewingTableMenu.this.clothSlot.set(clothStack);
                    }
                    else {
                        SewingTableMenu.this.clothSlot.set(ItemStack.EMPTY);
                    }
                }
                else {
                    SewingTableMenu.this.clothSlot.set(ItemStack.EMPTY);
                }

                access.execute((level, pos) -> {
                    long l = level.getGameTime();
                    if (SewingTableMenu.this.lastSoundTime != l) {
                        level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        SewingTableMenu.this.lastSoundTime = l;
                    }

                });
                super.onTake(player, resultStack);
            }
        });

        //creates player inventory
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        //creates player hotbar
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.SEWING_TABLE.get());
    }

    public void removed(@NotNull Player player) {
        super.removed(player);
        this.access.execute((p_39871_, p_39872_) -> this.clearContainer(player, this.inputContainer));
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        this.access.execute(((level, pos) -> {
            setupResultSlot(level, this.focusSlot.getItem(), this.clothSlot.getItem(), this.templateSlot.getItem());
        }));
    }

    private void setupResultSlot(Level level, ItemStack focusStack, ItemStack clothStack, ItemStack templateStack) {
        var repairing = focusStack.getItem() instanceof SpellArmorItem && focusStack.isDamaged();
        var duplicating = templateStack.getItem() instanceof SpellArmorItem;
        var tempResult = ItemStack.EMPTY;
        this.focusCost = 0;
        if (repairing && !duplicating && !clothStack.isEmpty()) {
            //repairing
            tempResult = focusStack.copy();
            tempResult.setDamageValue(Math.min(focusStack.getDamageValue() - 80, focusStack.getMaxDamage()));
            this.clothCost = 1;
        } else if (!repairing) {
            //crafting and duplicating
            Optional<SewingTableRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(SewingTableRecipe.Type.INSTANCE, this.inputContainer, level);

            SewingTableRecipe recipe;
            if (optional.isPresent()) {
                recipe = optional.get();
                if (!recipe.matches(this.inputContainer, level)) {
                    this.resultSlot.set(ItemStack.EMPTY);
                    return;
                }
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
                return;
            }

            this.focusCost = recipe.getFocusItem().getStack().getCount();
            this.clothCost = recipe.getClothItem().getStack().getCount();
            tempResult = recipe.getResultItem();
        }

        if (!tempResult.isEmpty() && clothStack.getCount() >= this.clothCost && focusStack.getCount() >= this.focusCost) {
            this.resultSlot.set(tempResult);
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
    }

    Runnable slotUpdateListener = () -> {};
    private final Container inputContainer = new SimpleContainer(3) {
        public void setChanged() {
            super.setChanged();
            SewingTableMenu.this.slotsChanged(this);
            SewingTableMenu.this.slotUpdateListener.run();
        }
    };
    private final Container outputContainer = new SimpleContainer(1) {
        public void setChanged() {
            super.setChanged();
            SewingTableMenu.this.slotUpdateListener.run();
        }
    };

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotNum) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotNum);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();


            if (slotNum == this.resultSlot.index) {
                if (!this.moveItemStackTo(slotItem, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItem, itemstack);
            } else if (slotNum != this.clothSlot.index && slotNum != this.focusSlot.index && slotNum != this.templateSlot.index) {
                if (!focusSlot.hasItem() && slotItem.getItem() != ModItems.SPELL_CLOTH.get()) {
                    if (!this.moveItemStackTo(slotItem, this.focusSlot.index, this.focusSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotItem.getItem() == ModItems.SPELL_CLOTH.get()) {
                    if (!this.moveItemStackTo(slotItem, this.clothSlot.index, this.clothSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotItem.getItem() instanceof SpellArmorItem || slotItem.getItem() instanceof DyeableArmorItem) {
                    if (!this.moveItemStackTo(slotItem, this.templateSlot.index, this.templateSlot.index + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNum >= 4 && slotNum < 31) {
                    if (!this.moveItemStackTo(slotItem, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNum >= 31 && slotNum < 40 && !this.moveItemStackTo(slotItem, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 4, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItem.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItem);
        }

        return itemstack;
    }
}
