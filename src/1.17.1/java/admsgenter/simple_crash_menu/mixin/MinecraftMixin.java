package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃屏幕;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.MemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow private static Minecraft instance;
    @Shadow private boolean pause;
    @Shadow private CrashReport delayedCrash;

    @Inject(at = @At(remap = false, target = "Lnet/minecraftforge/fmllegacy/server/ServerLifecycleHooks;handleExit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private static void crash(CrashReport 崩溃报告, CallbackInfo 信息) {
        ((MinecraftMixin) (Object) instance).pause = true;
        CrashReport 延迟崩溃 = ((MinecraftMixin) (Object) instance).delayedCrash;
        ((MinecraftMixin) (Object) instance).delayedCrash = null;
        if(instance.screen == null || instance.screen.getClass() != 崩溃屏幕.class || 延迟崩溃 != null) {
            if(延迟崩溃 != null && instance.level != null) {
                instance.level.disconnect();
                instance.clearLevel(instance.isLocalServer()? new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")):new ProgressScreen(true));
            }
            instance.setScreen(new 崩溃屏幕(延迟崩溃 == null? 崩溃报告:延迟崩溃));
        }
        信息.cancel();
    }

    @Inject(at = @At("RETURN"), method = "emergencySave")
    private void emergencySave(CallbackInfo 信息) { MemoryReserve.allocate(); }

    @Redirect(at = @At(ordinal = 1, target = "Lnet/minecraft/client/Minecraft;emergencySave()V", value = "INVOKE"), method = "run")
    private void emergencySave1(Minecraft 游戏) {}

    @Redirect(at = @At(ordinal = 2, target = "Lnet/minecraft/client/Minecraft;emergencySave()V", value = "INVOKE"), method = "run")
    private void emergencySave2(Minecraft 游戏) {}
}