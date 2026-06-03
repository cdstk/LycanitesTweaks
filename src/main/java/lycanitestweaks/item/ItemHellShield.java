package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.BaseCreatureEntity;
import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.Variant;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.pets.PetEntry;
import lycanitestweaks.capability.toggleableitem.IToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import lycanitestweaks.compat.ModLoadedUtil;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.handlers.features.item.ConfigurableItemHandler;
import lycanitestweaks.item.base.ItemPassive;
import lycanitestweaks.item.interfaces.IItemWithCreatureInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemHellShield extends ItemPassive implements IItemWithCreatureInfo {

    public static final DamageSource LIFE_LINK = new DamageSource("lifeLink").setDamageBypassesArmor();

    // Reset onLivingUpdate -> items add themselves when they tick (Vanilla onUpdate, Bauble worn tick, etc)
    private static final Map<EntityLivingBase, Collection<ItemStack>> tickingItemStacks = new HashMap<>();

    public ItemHellShield(String name) {
        super(name);
    }

    // Client -> Render Layer
    // Server -> Shield Effect
    public static Collection<ItemStack> getTickedStacks(EntityLivingBase entityLivingBase) {
        return tickingItemStacks.get(entityLivingBase);
    }

    @Override
    public boolean isEnabled() {
        return ForgeConfigHandler.server.customStaffConfig.registerSpecialBossDrops;
    }

    @Override
    public boolean hasSubscriber() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@Nonnull ItemStack itemStack, World world, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag);

        StringBuilder rawStrings = new StringBuilder();
        rawStrings.append(I18n.format("item.lycanitestweaks.hellshield.description"));

        CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature("asmodeus");
        if(creatureInfo != null && this.getLevel(itemStack) > 0) {
            rawStrings.append("\n");
            Variant variant = creatureInfo.getSubspecies(0).getVariant(this.getEntityVariant(itemStack));
            if (variant != null) rawStrings.append(variant.getTitle()).append(" ");

            rawStrings.append(creatureInfo.getTitle()).append(" ");
            rawStrings.append(I18n.format("entity.level")).append(" ").append(this.getLevel(itemStack));
        }

        rawStrings.append("\n");
        if(GuiScreen.isShiftKeyDown()) {
            if(ModLoadedUtil.baubles.isLoaded()) {
                ConfigurableItemHandler.EquipmentSlot slots = ConfigurableItemHandler.getItemSlot(itemStack);
                if(slots != null && slots.baubleAttributes) {
                    rawStrings.append(I18n.format("lycanitestweaks.ability.attributebauble.tooltip0")).append("\n");
                }
            }
            rawStrings.append(I18n.format("lycanitestweaks.ability.hellshield.tooltip0")).append("\n");
            ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(itemStack);
            if(stats != null) {
                rawStrings.append(I18n.format("lycanitestweaks.ability.hellshield.tooltip1", stats.defense)).append("\n");
            }
        }
        else {
            rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
        }

        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        EntityEquipmentSlot equipmentSlot = super.getEquipmentSlot(stack);
        return equipmentSlot != null ? equipmentSlot : EntityEquipmentSlot.OFFHAND;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        if(repairStack.getItem() == Items.NETHER_STAR) return true;
        return super.getIsRepairable(itemStack, repairStack);
    }

    @Override
    public boolean isToggleable() {
        return true;
    }

    @Override
    public void tickAbility(ItemStack stack, EntityLivingBase entity) {
        super.tickAbility(stack, entity);

        IToggleableItem toggleableItem = ToggleableItem.getForItemStack(stack);
        if(toggleableItem != null) {
            if(toggleableItem.isAbilityToggled()) {
                Collection<ItemStack> ticking = tickingItemStacks.computeIfAbsent(entity, entityLivingBase -> new ArrayList<>());
                ticking.add(stack);
            }
        }
    }


    // ==================================================
    //                    Item Use
    // ==================================================
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if(!player.world.isRemote) {
            IToggleableItem toggleableItem = ToggleableItem.getForItemStack(itemStack);
            if(toggleableItem != null) {
                toggleableItem.toggleAbility(!toggleableItem.isAbilityToggled(), itemStack, player);
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }

    // Resets
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        tickingItemStacks.put(event.getEntityLiving(), new ArrayList<>());
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        tickingItemStacks.clear();
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event){
        if(event.isCanceled()) return;
        if(event.getSource() == LIFE_LINK) return;
        if(event.getSource().isUnblockable()) return;
        EntityLivingBase victim = event.getEntityLiving();

        List<EntityLivingBase> linkedEntities = new ArrayList<>();
        // Add player (victim) -> add bound pets
        if(victim instanceof EntityPlayer) {
            if(getTickedStacks(victim) == null || getTickedStacks(victim).isEmpty()) return;

            ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer((EntityPlayer) victim);
            if (extendedPlayer != null) {
                linkedEntities.add(victim);
                for (PetEntry petEntry : extendedPlayer.petManager.entries.values()) {
                    if (petEntry.spawningActive && petEntry.active) {
                        if (petEntry.entity instanceof EntityLivingBase && petEntry.entity.isEntityAlive()) {
                            linkedEntities.add((EntityLivingBase) petEntry.entity);
                        }
                    }
                }
            }
        }
        // Add owner -> add bound pets (includes victim)
        else if(victim instanceof BaseCreatureEntity) {
            BaseCreatureEntity creature = (BaseCreatureEntity) victim;
            if(creature.isBoundPet()) {
                PetEntry boundPet = creature.getPetEntry();
                if(boundPet.host instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) boundPet.host;
                    if(getTickedStacks(player) == null || getTickedStacks(player).isEmpty()) return;

                    ExtendedPlayer extendedPlayer = ExtendedPlayer.getForPlayer(player);
                    if (extendedPlayer != null) {
                        linkedEntities.add(player);
                        for (PetEntry petEntry : extendedPlayer.petManager.entries.values()) {
                            if (petEntry.spawningActive && petEntry.active) {
                                if (petEntry.entity instanceof EntityLivingBase && petEntry.entity.isEntityAlive()) {
                                    linkedEntities.add((EntityLivingBase) petEntry.entity);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (linkedEntities.size() > 1) {
            // Divide between
            float lifeLinkDamage = event.getAmount() / linkedEntities.size();
            // Original damage source to victim
            event.setAmount(lifeLinkDamage);
            linkedEntities.remove(victim);
            // Life Link damage source to others
            linkedEntities.forEach(linkedEntity -> linkedEntity.attackEntityFrom(LIFE_LINK, lifeLinkDamage));
        }
    }

    // When Defence Attribute for all entities is not enabled
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if(ForgeConfigHandler.minorFeaturesConfig.lycanitesAttributesForAll) return;
        EntityLivingBase victim = event.getEntityLiving();
        if(getTickedStacks(victim) == null || getTickedStacks(victim).isEmpty()) return;

        if(event.getAmount() > 1F) {
            float damageReduction = 0F;
            for (ItemStack itemStack : getTickedStacks(victim)) {
                ConfigurableItemHandler.ItemStats stats = ConfigurableItemHandler.getItemStats(itemStack);
                if(stats != null) {
                    float defense = (float) stats.defense;
                    if(victim.isActiveItemStackBlocking()) {
                        defense = Math.max(1, defense);
                        defense *= 4F;
                    }
                    damageReduction = Math.max(damageReduction, defense);
                }
            }
            event.setAmount(Math.max(1F, event.getAmount() - damageReduction));
        }
    }

    @Override
    public String getCreatureTypeName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CREATURE_TYPE_NBT)) return nbt.getString(CREATURE_TYPE_NBT);
        return "";
    }

    @Override
    public String getCustomName(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(CUSTOM_NAME_NBT)) return nbt.getString(CUSTOM_NAME_NBT);
        return "";
    }

    @Override
    public int getLevel(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(MOB_LEVEL_NBT)) return nbt.getInteger(MOB_LEVEL_NBT);
        return -1;
    }

    @Override
    public int getEntitySubspecies(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(SUBSPECIES_NBT)) return nbt.getInteger(SUBSPECIES_NBT);
        return -1;
    }

    @Override
    public int getEntityVariant(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(VARIANT_NBT)) return nbt.getInteger(VARIANT_NBT);
        return -1;
    }

    @Override
    public String getEntityVariantRarity(ItemStack itemStack){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        if(nbt.hasKey(VARIANT_RARITY_NBT)) return nbt.getString(VARIANT_RARITY_NBT);
        return COMMON_VALUE;
    }

    @Override
    public void setCreatureTypeName(ItemStack itemStack, String type){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CREATURE_TYPE_NBT, type);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setCustomName(ItemStack itemStack, String name){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(CUSTOM_NAME_NBT, name);
        itemStack.setTagCompound(nbt);
    }

    @Override
    public void setLevel(ItemStack itemStack, int level){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(MOB_LEVEL_NBT, level);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntitySubspecies(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(SUBSPECIES_NBT, index);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntityVariant(ItemStack itemStack, int index){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setInteger(VARIANT_NBT, index);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }

    @Override
    public void setEntityVariantRarity(ItemStack itemStack, String rarity){
        NBTTagCompound nbt = this.getTagCompound(itemStack);
        nbt.setString(VARIANT_RARITY_NBT, rarity);
        itemStack.setTagCompound(nbt);
        ConfigurableItemHandler.resetItemStats(itemStack);
    }
}
