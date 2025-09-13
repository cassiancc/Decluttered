package cc.cassian.decluttered.duck;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public interface PlayerInventoryAccess {

    ArrayList<ItemStack> getAllMainStacksWithTag(TagKey<Item> tag);

    ArrayList<ItemStack> getAllMainStacksOf(Collection<Item> items);
}
