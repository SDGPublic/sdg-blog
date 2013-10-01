package sdg.blog.protectids.helper;

import sdg.blog.protectids.domain.BaseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SetIdHelper {

    public static void setId(BaseEntity persistentObject, Long id) {
        try {
            Method setIdMethod = findMethod(persistentObject.getClass(),"setId");
            setIdMethod.setAccessible(true);
            setIdMethod.invoke(persistentObject, id);
        }
        catch (IllegalAccessException|InvocationTargetException e) {
            System.out.println(e);
        }
    }

    private static Method findMethod(Class entityClass, String methodName) {

        Method method = null;
        while (entityClass.getSuperclass() != null && method == null) {
            entityClass = entityClass.getSuperclass();
            try {
                method = entityClass.getDeclaredMethod(methodName,Long.class);
            }
            catch (NoSuchMethodException e) {
                System.out.println(e);
            }
        }

        return method;
    }
}

