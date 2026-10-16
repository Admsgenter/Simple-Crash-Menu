package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃屏幕;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow static Minecraft instance;
    @Shadow private Supplier<CrashReport> delayedCrash;

    @Inject(at = @At(remap = false, target = "Lnet/minecraftforge/server/ServerLifecycleHooks;handleExit(I)V", value = "INVOKE"), cancellable = true, method = "crash")
    private static void crash(CrashReport 崩溃报告, CallbackInfo 信息) {
        ((MinecraftMixin) (Object) instance).delayedCrash = null;
        instance.setScreen(new 崩溃屏幕(崩溃报告));
        信息.cancel();
    }
}