package cc.cassian.decluttered.tags;

import net.minecraft.item.Item;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static final TagKey<Item> SWAPPABLE = itemTagKey("swappable");

    public static TagKey<Item> itemTagKey(String id) {
        return itemTagKey("decluttered", id);
    }

    public static TagKey<Item> itemTagKey(String namespace, String id) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, id));
    }
}
