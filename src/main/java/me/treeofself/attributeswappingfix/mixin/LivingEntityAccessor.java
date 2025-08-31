package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("getEquipmentChanges")
    Map<EquipmentSlot, ItemStack> invokeGetEquipmentChanges();
}