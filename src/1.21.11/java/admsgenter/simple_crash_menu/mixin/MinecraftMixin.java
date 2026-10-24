package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃屏幕;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.MemoryReserve;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Supplier;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow static Minecraft instance;
    @Shadow private volatile boolean pause;
    @Shadow private Supplier<CrashReport> delayedCrash;

    @ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "crash")
    private static Minecraft crash(Minecraft 游戏) { return null; }

    @Inject(at = @At(remap = false, target = "Lnet/neoforged/neoforge/server/ServerLifecycleHooks;handleExit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private static void crash(Minecraft 游戏, File 文件, CrashReport 崩溃报告, CallbackInfo 信息) {
        ((MinecraftMixin) (Object) instance).pause = true;
        Supplier<CrashReport> 延迟崩溃 = ((MinecraftMixin) (Object) instance).delayedCrash;
        ((MinecraftMixin) (Object) instance).delayedCrash = null;
        if(instance.screen == null || instance.screen.getClass() != 崩溃屏幕.class || 延迟崩溃 != null) {
            if(延迟崩溃 != null && 游戏.level != null) {
                游戏.level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
                if(游戏.isLocalServer()) 游戏.disconnectWithSavingScreen();
                else 游戏.disconnectWithProgressScreen();
            }
            instance.setScreen(new 崩溃屏幕(延迟崩溃 == null? 崩溃报告:延迟崩溃.get()));
        }
        信息.cancel();
    }

    @Inject(at = @At("RETURN"), method = "emergencySave")
    private void emergencySave(CallbackInfo 信息) { MemoryReserve.allocate(); }

    @Redirect(at = @At(target = "Lnet/minecraft/client/Minecraft;emergencySaveAndCrash(Lnet/minecraft/CrashReport;)V", value = "INVOKE"), method = "run")
    private void emergencySaveAndCrash(Minecraft 游戏, CrashReport 报告) { Minecraft.crash(null, 游戏.gameDirectory, 游戏.fillReport(报告)); }
}