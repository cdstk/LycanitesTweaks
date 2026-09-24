package lycanitestweaks.mixin.lycanitesmobspatches.core.assetmanager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lycanitesmobs.LycanitesMobs;
import com.lycanitesmobs.ObjectManager;
import com.lycanitesmobs.core.helpers.JSONHelper;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ElementManager;
import com.lycanitesmobs.core.item.equipment.EquipmentPartManager;
import com.lycanitesmobs.core.item.equipment.ItemEquipmentPart;
import com.lycanitesmobs.core.item.equipment.features.EquipmentFeature;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(EquipmentPartManager.class)
public abstract class EquipmentPartManager_ReloadFixMixin {

    // Credit https://github.com/Fresh-glitch for reporting new instance replacing instead of updating
    @WrapOperation(
            method = "parseJson",
            at = @At(value = "INVOKE", target = "Lcom/lycanitesmobs/core/item/equipment/ItemEquipmentPart;loadFromJSON(Lcom/google/gson/JsonObject;)V"),
            remap = false
    )
    private void lycanitesTweaks_lycanitesMobsEquipmentPartManager_parseJsonGetOrCreateInstance(ItemEquipmentPart instance, JsonObject json, Operation<Void> original, @Local LocalRef<ItemEquipmentPart> newInstance){
        Item item = ObjectManager.getItem(json.get("itemName").getAsString());
        if(item instanceof ItemEquipmentPart) {
            ItemEquipmentPart previousInstance = (ItemEquipmentPart) item;
            lycanitesTweaks$reloadFromJSON(previousInstance, json);
            newInstance.set(previousInstance);
        }
        else {
            original.call(instance, json);
        }
    }

    // Copy of original without duplicating drops and properly resetting feature list
    @Unique
    private static void lycanitesTweaks$reloadFromJSON(ItemEquipmentPart itemEquipmentPart, JsonObject json) {
        itemEquipmentPart.itemName = json.get("itemName").getAsString();
        itemEquipmentPart.slotType = json.get("slotType").getAsString();

        if(json.has("dropChance"))
            itemEquipmentPart.dropChance = json.get("dropChance").getAsFloat();

        if(json.has("levelMin"))
            itemEquipmentPart.levelMin = json.get("levelMin").getAsInt();

        if(json.has("levelMax"))
            itemEquipmentPart.levelMax = json.get("levelMax").getAsInt();

        // Elements:
        itemEquipmentPart.elements.clear();
        List<String> elementNames = new ArrayList<>();
        if(json.has("elements")) {
            elementNames = JSONHelper.getJsonStrings(json.get("elements").getAsJsonArray());
        }
        for(String elementName : elementNames) {
            ElementInfo element = ElementManager.getInstance().getElement(elementName);
            if (element == null) {
                throw new RuntimeException("[Equipment] Unable to initialise Equipment Part " + itemEquipmentPart.getTranslationKey() + " as the element " + elementName + " cannot be found.");
            }
            itemEquipmentPart.elements.add(element);
        }

        // Features:
        itemEquipmentPart.features.clear();
        if(json.has("features")) {
            JsonArray jsonArray = json.get("features").getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject featureJson = jsonElement.getAsJsonObject();
                if (!itemEquipmentPart.addFeature(EquipmentFeature.createFromJSON(featureJson))) {
                    LycanitesMobs.logWarning("", "[Equipment] The feature " + featureJson + " was unable to be added, check the JSON format.");
                }
            }
        }
        itemEquipmentPart.sortFeatures();
    }
}
