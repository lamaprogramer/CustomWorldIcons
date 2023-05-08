package net.iamaprogrammer.customworldicons.mixin;

import net.iamaprogrammer.customworldicons.gui.screen.WorldIconScreen;
import net.iamaprogrammer.customworldicons.util.ButtonStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.nio.file.Path;

@Mixin(MoreOptionsDialog.class)
public abstract class MoreOptionsDialogMixin {
	@Shadow private int parentWidth;
	public ButtonWidget worldIconsButton;


	@Inject(method = "init", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void inject(CreateWorldScreen parent, MinecraftClient client, TextRenderer textRenderer, CallbackInfo ci) {
		int j = this.parentWidth / 2 + 5;

		this.worldIconsButton = (ButtonWidget)parent.addDrawableChild(new ButtonWidget(j, 151, 150, 20, Text.translatable("world.create.icon.title"), (button) -> {
			Path p = Path.of(new File(client.runDirectory, "worldicons/").toURI());
			client.setScreen(new WorldIconScreen(client, parent, p, Text.translatable("world.create.icon.title")));

		}));
		this.worldIconsButton.visible = false;
		ButtonStorage.addIcon = this.worldIconsButton;
	}
}


