package admsgenter.simple_crash_menu.mixin;

import net.minecraft.crash.ReportedException;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReportedException.class)
public class ReportedExceptionMixin extends RuntimeException {
    @Override
    public StackTraceElement[] getStackTrace() { return getCause().getStackTrace(); }
}