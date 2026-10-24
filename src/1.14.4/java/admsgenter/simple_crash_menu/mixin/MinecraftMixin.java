package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃屏幕;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DirtMessageScreen;
import net.minecraft.client.gui.screen.WorkingScreen;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow private boolean hasCrashed;
    @Shadow private boolean pause;
    @Shadow private CrashReport delayedCrash;

    @Shadow protected abstract void init();

    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;destroy()V", value = "INVOKE"), method = "run")
    private void destroy(Minecraft 游戏) {}

    @Unique
    private void SCM$崩溃(CrashReport 崩溃报告, CallbackInfo 信息) {
        pause = true;
        CrashReport 延迟崩溃 = delayedCrash;
        delayedCrash = null;
        hasCrashed = false;
        if(((Minecraft) (Object) this).screen == null || ((Minecraft) (Object) this).screen.getClass() != 崩溃屏幕.class || 延迟崩溃 != null) {
            if(延迟崩溃 != null && ((Minecraft) (Object) this).level != null) {
                ((Minecraft) (Object) this).level.disconnect();
                ((Minecraft) (Object) this).clearLevel(((Minecraft) (Object) this).isLocalServer()? new DirtMessageScreen(new TranslationTextComponent("menu.savingLevel")):new WorkingScreen());
            }
            ((Minecraft) (Object) this).setScreen(new 崩溃屏幕(延迟崩溃 == null? 崩溃报告:延迟崩溃));
        }
        信息.cancel();
    }

    @Inject(at = @At(remap = false, target = "Ljava/lang/System;exit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private void exit(CrashReport 崩溃报告, CallbackInfo 信息) { SCM$崩溃(崩溃报告, 信息); }

    @Inject(at = @At(remap = false, target = "Lnet/minecraftforge/fml/server/ServerLifecycleHooks;handleExit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private void handleExit(CrashReport 崩溃报告, CallbackInfo 信息) { SCM$崩溃(崩溃报告, 信息); }

    @Inject(at = @At("RETURN"), method = "emergencySave")
    private void emergencySave(CallbackInfo 信息) { Minecraft.reserve = new byte[10485760]; }

    @Redirect(at = @At(ordinal = 1, target = "Lnet/minecraft/client/Minecraft;emergencySave()V", value = "INVOKE"), method = "run")
    private void emergencySave1(Minecraft 游戏) {}

    @Redirect(at = @At(ordinal = 2, target = "Lnet/minecraft/client/Minecraft;emergencySave()V", value = "INVOKE"), method = "run")
    private void emergencySave2(Minecraft 游戏) {}

    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;init()V", value = "INVOKE"), method = "run")
    private void init(Minecraft 游戏) { if(游戏.options == null) init(); }
}