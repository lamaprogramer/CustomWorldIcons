package net.iamaprogrammer.customworldicons.mixin;

import net.minecraft.client.gui.screen.world.WorldListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldListWidget.WorldEntry.class)
public class WorldListWidgetMixin {

    @Redirect(method = "getIconTexture", at = @At(value = "INVOKE", target = "Lorg/apache/commons/lang3/Validate;validState(ZLjava/lang/String;[Ljava/lang/Object;)V"), allow = 2)
    private void injected(boolean expression, String message, Object[] values) {
    }
}
