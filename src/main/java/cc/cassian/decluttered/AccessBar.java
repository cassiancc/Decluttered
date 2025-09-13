package cc.cassian.decluttered;

import java.util.ArrayList;

import cc.cassian.decluttered.tags.ModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class AccessBar{

    private final MinecraftClient client;
    private final SoundEvent selectionSoundEvent;
    private final ArrayList<Item> stacks = new ArrayList<>();
    private int selectedAccessSlotIndex = 0;
    private final Identifier textures;

    public AccessBar(SoundEvent selectionSoundEvent, Identifier textures, MinecraftClient client){
        this.client = client;
        this.selectionSoundEvent = selectionSoundEvent;
        this.textures = textures;
    }

    public void updateAccessStacks(){
        if (client.player==null) return;
        Item heldItem = client.player.getMainHandStack().getItem();
        stacks.clear();

        ArrayList<Item> items = new ArrayList<>();

        if (heldItem.getDefaultStack().isIn(ModTags.SWAPPABLE)) {
            stacks.clear();
            heldItem.getDefaultStack().streamTags().forEach((itemTagKey -> {
                var id = itemTagKey.id();
                if (id.getNamespace().equals("decluttered") && !id.getPath().equals("swappable")) {
                    Registries.ITEM.iterateEntries(itemTagKey).forEach(itemRegistryEntry -> {
                        var value = itemRegistryEntry.value();
                        items.add(value);
                    });
                }
            }));
        }
        if (items.isEmpty()) {
            items.add(heldItem);
        }

        stacks.addAll(items);
    }

    public void scrollInAccessBar(double scrollAmount) {
        int barSize = stacks.size();
        int slotsToScroll = (int)Math.signum(scrollAmount);
  
        selectedAccessSlotIndex = Math.floorMod(selectedAccessSlotIndex -slotsToScroll, barSize);
    }

    public void selectItem(){
        PlayerInventory inv = client.player.getInventory();
        int slotSwapIsLockedTo = Decluttered.CONFIG.lockSwappingToSlot;
        int slotToSwap = !(slotSwapIsLockedTo<1||slotSwapIsLockedTo>PlayerInventory.getHotbarSize()) ? slotSwapIsLockedTo-1 : inv.getSelectedSlot();
        Item selectedHotbarSlotStack = inv.getStack(slotToSwap).getItem();
        Item selectedAccessbarStack = stacks.get(selectedAccessSlotIndex);

        if(!selectedAccessbarStack.equals(Items.AIR)){
            client.player.setStackInHand(Hand.MAIN_HAND, new ItemStack(selectedAccessbarStack, client.player.getMainHandStack().getCount()));

            client.getSoundManager().play(PositionedSoundInstance.master(selectionSoundEvent,1.0F,1.0F));
        }
    }

    public void resetSelection(){
        PlayerInventory inv = client.player.getInventory();
        if(Decluttered.CONFIG.heldItemSelected) {
            updateAccessStacks();
            selectedAccessSlotIndex = stacks.indexOf(inv.getStack(inv.getSelectedSlot()).getItem());
        }else{
            selectedAccessSlotIndex = 0;
        }
    }

    public int getSelectedAccessSlotIndex() {
        return selectedAccessSlotIndex;
    }

    public void setSelectedAccessSlotIndex(int selectedAccessSlotIndex) {
        this.selectedAccessSlotIndex = selectedAccessSlotIndex;
    }

    public ArrayList<Item> getStacks() {
        return stacks;
    }

    public Identifier getTextures() {
        return textures;
    }
}