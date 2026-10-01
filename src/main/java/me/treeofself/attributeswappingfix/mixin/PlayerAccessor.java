package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface PlayerAccessor {
	@Accessor("lastItemInMainHand")
	ItemStack getLastItemInMainHand();

	@Accessor("lastItemInMainHand")
	void setLastItemInMainHand(ItemStack stack);
}
