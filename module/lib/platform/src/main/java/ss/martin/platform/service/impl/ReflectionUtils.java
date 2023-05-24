package ss.martin.platform.service.impl;

/**
 * Reflection utilities implementation.
 * @author ss
 */
public class ReflectionUtils {
    /**
     * Check if class has superclass.
     * @param clazz target class.
     * @param superClass super class.
     * @return true if has.
     * @throws Exception error.
     */
    public static boolean hasSuperClass(Class clazz, Class superClass) throws Exception {
        Class curClass = clazz;
        while (curClass.getSuperclass() != null) {
            if (curClass.getSuperclass().equals(superClass)) {
                return true;
            }
            curClass = curClass.getSuperclass();
        }
        return false;
    }
}
