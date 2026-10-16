package admsgenter.simple_crash_menu.mixin;

import admsgenter.simple_crash_menu.崩溃菜单;
import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrashReportCategory.class)
public class CrashReportCategoryMixin {
    @Redirect(at = @At(target = "Ljava/lang/Thread;getStackTrace()[Ljava/lang/StackTraceElement;", value = "INVOKE"), method = "fillInStackTrace")
    private StackTraceElement[] getStackTrace(Thread 线程) { return 崩溃菜单.移除堆栈(线程.getStackTrace()); }
}