package ch.cloudcoins.security.control;

import ch.cloudcoins.security.entity.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginContextHolder {

    static final ThreadLocal<LoginContext> holder = new InheritableThreadLocal<>();

    private LoginContextHolder() {
    }

    public static LoginContext get() {
        return holder.get();
    }

    public static void set(LoginContext context) {
        holder.set(context);
    }

    public static void clearContext() {
        holder.remove();
    }
}
