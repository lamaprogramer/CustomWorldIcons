package net.iamaprogrammer.customworldicons.mixin;

import net.iamaprogrammer.customworldicons.gui.screen.WorldIconScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow private boolean hasWorldIcon;

    @Shadow private long lastWorldIconUpdate;

    @Shadow @Final private static Logger LOGGER;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;updateWorldIcon()V"))
    private void injected(GameRenderer instance) {
        updateIcon(WorldIconScreen.SELECTED_ICON, WorldIconScreen.SELECTED_ICON != null);
        WorldIconScreen.SELECTED_ICON = null;
    }

    public void updateIcon(String iconPath, boolean isCustom) {

        if (!this.hasWorldIcon && this.client.isInSingleplayer()) {
            long l = Util.getMeasuringTimeMs();
            if (l - this.lastWorldIconUpdate >= 1000L) {
                this.lastWorldIconUpdate = l;
                IntegratedServer integratedServer = this.client.getServer();
                if (integratedServer != null && !integratedServer.isStopped()) {
                    integratedServer.getIconFile().ifPresent((path) -> {
                        if (Files.isRegularFile(path, new LinkOption[0])) {
                            this.hasWorldIcon = true;
                        } else {
                            imagePathToIcon(iconPath, path, isCustom);
                        }

                    });
                }
            }
        }
    }

    private void imagePathToIcon(String path, Path destination, boolean isCustom) {
        try {
            NativeImage nativeImage;
            if (isCustom) {
                InputStream s = Files.newInputStream(Paths.get(path));
                nativeImage = NativeImage.read(s);
                s.close();
                genImage(nativeImage, destination);
            } else {
                if (this.client.worldRenderer.getCompletedChunkCount() > 10 && this.client.worldRenderer.isTerrainRenderComplete()) {
                    nativeImage = ScreenshotRecorder.takeScreenshot(this.client.getFramebuffer());
                    genImage(nativeImage, destination);
                }
            }

        } catch (Exception iOException) {
            LOGGER.warn("Couldn't save auto screenshot", iOException);
        }
    }

    private void genImage(NativeImage nativeImage, Path destination) {
        Util.getIoWorkerExecutor().execute(() -> {
            int i = nativeImage.getWidth();
            int j = nativeImage.getHeight();
            int k = 0;
            int l = 0;
            if (i > j) {
                k = (i - j) / 2;
                i = j;
            } else {
                l = (j - i) / 2;
                j = i;
            }
            try {
                NativeImage nativeImage2 = new NativeImage(256, 256, false);
                try {
                    if (nativeImage.getWidth() > 256 || nativeImage.getHeight() > 256) {
                        nativeImage.resizeSubRectTo(k, l, i, j, nativeImage2);
                        nativeImage2.writeTo(destination);
                    } else {
                        nativeImage.writeTo(destination);
                    }
                } catch (Throwable var15) {
                    try {
                        nativeImage2.close();
                    } catch (Throwable var14) {
                        var15.addSuppressed(var14);
                    }

                    throw var15;
                }
                nativeImage2.close();
            } catch (IOException iOException) {
                LOGGER.warn("Couldn't save auto screenshot", iOException);
            } finally {
                nativeImage.close();
            }
        });
    }
}
