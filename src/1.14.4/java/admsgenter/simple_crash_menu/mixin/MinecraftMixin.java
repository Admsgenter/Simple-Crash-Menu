package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃屏幕;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow private boolean hasCrashed;
    @Shadow private CrashReport delayedCrash;

    @Shadow protected abstract void init();

    @Shadow public abstract void setScreen(Screen 屏幕);

    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;destroy()V", value = "INVOKE"), method = "run")
    private void destroy(Minecraft 游戏) {}

    @Inject(at = @At(remap = false, target = "Ljava/lang/System;exit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private void exit(CrashReport 崩溃报告, CallbackInfo 信息) {
        delayedCrash = null;
        hasCrashed = false;
        setScreen(new 崩溃屏幕(崩溃报告));
        信息.cancel();
    }

    @Inject(at = @At(remap = false, target = "Lnet/minecraftforge/fml/server/ServerLifecycleHooks;handleExit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private void handleExit(CrashReport 崩溃报告, CallbackInfo 信息) {
        delayedCrash = null;
        hasCrashed = false;
        setScreen(new 崩溃屏幕(崩溃报告));
        信息.cancel();
    }

    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;init()V", value = "INVOKE"), method = "run")
    private void init(Minecraft 游戏) { if(游戏.options == null) init(); }
}