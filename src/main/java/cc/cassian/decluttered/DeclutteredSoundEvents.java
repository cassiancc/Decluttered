package cc.cassian.decluttered;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import cc.cassian.decluttered.config.ModConfig;

import java.util.ArrayList;

import static cc.cassian.decluttered.Decluttered.MOD_ID;

public class DeclutteredSoundEvents {
    public static SoundEvent selectInAccess1;
    public static SoundEvent selectInAccess2;

    private static final ArrayList<SoundEvent> soundEvents = new ArrayList<SoundEvent>();

    public static SoundEvent register(Identifier id){
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerAll(){
        soundEvents.add(register(Identifier.of(MOD_ID,"select0")));
        soundEvents.add(register(Identifier.of(MOD_ID,"select1")));
        soundEvents.add(register(Identifier.of(MOD_ID,"select2")));
        soundEvents.add(register(Identifier.of(MOD_ID,"select3")));
    }

    public static void updateSoundEventsAsConfigured(){
        selectInAccess1 = soundEvents.get(1);
        selectInAccess2 = soundEvents.get(2);
    }
    
}
