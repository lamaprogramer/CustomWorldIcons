package net.iamaprogrammer.customworldicons.mixin;

import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldIcon.class)
public abstract class WorldListWidgetMixin {

    @Shadow protected abstract void assertOpen();

    @Shadow @Nullable private NativeImageBackedTexture texture;

    @Shadow @Final private TextureManager textureManager;

    @Shadow @Final private Identifier id;

    @Shadow public abstract void destroy();

    /**
     * @author Iamaprogrammer
     * @reason Remove icon limitations
     */
    @Overwrite
    public void load(NativeImage image) {
        try {
            this.assertOpen();
            if (this.texture == null) {
                this.texture = new NativeImageBackedTexture(image);
            } else {
                this.texture.setImage(image);
                this.texture.upload();
            }

            this.textureManager.registerTexture(this.id, this.texture);
        } catch (Throwable var3) {
            image.close();
            this.destroy();
            throw var3;
        }
    }
}
