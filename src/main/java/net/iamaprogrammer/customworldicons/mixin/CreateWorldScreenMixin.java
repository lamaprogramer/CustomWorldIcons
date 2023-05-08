package net.iamaprogrammer.customworldicons.mixin;

import net.iamaprogrammer.customworldicons.gui.screen.WorldIconScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.nio.file.Path;

@Mixin(targets = {"net/minecraft/client/gui/screen/world/CreateWorldScreen$WorldTab"})
public class CreateWorldScreenMixin extends GridScreenTab {
	private ButtonWidget worldIconsButton;

	public CreateWorldScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void inject(CreateWorldScreen createWorldScreen, CallbackInfo ci, GridWidget.Adder adder) {
		MinecraftClient client = MinecraftClient.getInstance();
		this.worldIconsButton = (ButtonWidget)adder.add(ButtonWidget.builder(Text.translatable("world.create.icon.title"), (button) -> {
			Path p = Path.of(new File(client.runDirectory, "worldicons/").toURI());
			client.setScreen(new WorldIconScreen(client, createWorldScreen, p, Text.translatable("world.create.icon.title")));
		}).width(308).build(), 2);
	}
}


