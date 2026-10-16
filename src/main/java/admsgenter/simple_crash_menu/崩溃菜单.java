package admsgenter.simple_crash_menu;

import net.minecraft.client.main.Main;
import net.minecraftforge.fml.common.Mod;

@Mod("simple_crash_menu")
public class 崩溃菜单 {
    public static StackTraceElement[] 移除堆栈(StackTraceElement[] 堆栈表) {
        String 名称 = Main.class.getName();
        for(int 索引 = 0; 索引 < 堆栈表.length; 索引++)
            if(名称.equals(堆栈表[索引].getClassName()) && 堆栈表[索引].getMethodName().endsWith("SCM$运行")) {
                StackTraceElement[] 新堆栈表 = new StackTraceElement[堆栈表.length-1];
                System.arraycopy(堆栈表, 0, 新堆栈表, 0, 索引);
                System.arraycopy(堆栈表, 索引+1, 新堆栈表, 索引, 新堆栈表.length-索引);
                return 新堆栈表;
            }
        return 堆栈表;
    }
}