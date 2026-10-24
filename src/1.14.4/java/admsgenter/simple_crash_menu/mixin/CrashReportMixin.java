package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃菜单;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Unique final String SCM$线程 = Thread.currentThread().getName();

    @ModifyVariable(argsOnly = true, at = @At("HEAD"), method = "<init>")
    private static Throwable init(Throwable 异常) {
        异常.setStackTrace(崩溃菜单.移除堆栈(异常.getStackTrace()));
        return 异常;
    }

    @Override
    public String toString() { return SCM$线程; }
}