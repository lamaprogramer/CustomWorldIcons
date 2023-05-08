package net.iamaprogrammer.customworldicons.mixin;


import net.iamaprogrammer.customworldicons.util.ButtonStorage;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {

    @Shadow private boolean moreOptionsOpen;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "setMoreOptionsOpen(Z)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void inject(boolean moreOptionsOpen, CallbackInfo ci) {
        if (ButtonStorage.addIcon != null) {
            ButtonStorage.addIcon.visible = this.moreOptionsOpen;
        }
    }
}
