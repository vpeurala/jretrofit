package org.jretrofit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Support for easy reflection usage. This API makes
 * reflection calls feel more natural and it also
 * wraps all exceptions that can be thrown from
 * reflection into a {@link ReflectException}.
 * 
 * @author Ville Peurala
 */
public class Reflect {
    public static RObject on(Class<?> wrappedClass) {
        return new RObject(wrappedClass);
    }

    public static RObject on(Object wrappedObject) {
        return new RObject(wrappedObject);
    }

    public static class RObject {
        private final Object targetObject;
        private final Class<?> klass;

        public RObject(Class<?> wrappedClass) {
            requireNotNull(wrappedClass,
                    "Class passed to RObject constructor was null.");
            this.targetObject = null;
            this.klass = wrappedClass;
        }

        public RObject(Object wrappedObject) {
            requireNotNull(wrappedObject,
                    "Object passed to RObject constructor was null.");
            this.targetObject = wrappedObject;
            this.klass = wrappedObject.getClass();
        }

        public boolean isAssociatedWithObjectReference() {
            return targetObject != null;
        }

        public RMethod<Object> method(final String name) {
            return method(name, Object.class);
        }

        public <ReturnType> RMethod<ReturnType> method(String name,
                Class<ReturnType> returnType) {
            final Set<Method> allMethods = allMethods(klass);
            final Set<Method> matchingMethods = new HashSet<Method>();
            for (final Method m : allMethods) {
                if (isMethodCompatible(name, returnType, m)) {
                    matchingMethods.add(m);
                }
            }
            if (matchingMethods.isEmpty()) {
                throw new ReflectException("No method named '" + name
                        + "' on target object '" + targetObject
                        + "' of class '" + klass + "'.");
            }
            return new RMethod<ReturnType>(matchingMethods, targetObject);
        }

        private <ReturnType> boolean isMethodCompatible(String name,
                Class<ReturnType> returnType, final Method m) {
            return m.getName().equals(name)
                    && (m.getReturnType() == Void.TYPE || returnType
                            .isAssignableFrom(autoboxIfNecessary(m
                                    .getReturnType())));
        }

        public RField<Object> field(String name) {
            return field(name, null);
        }

        public <Type> RField<Type> field(String name, Class<Type> type) {
            final Set<Field> allFields = allFields(klass);
            final Set<Field> matchingFields = new HashSet<Field>();
            for (final Field f : allFields) {
                if (f.getName().equals(name)
                        && (type == null || autoboxIfNecessary(f.getType())
                                .equals(type))) {
                    matchingFields.add(f);
                }
            }
            if (matchingFields.isEmpty()) {
                throw new ReflectException("No field named '" + name
                        + "' on target object '" + targetObject
                        + "' of class '" + klass + "'.");
            }
            return new RField<Type>(matchingFields, targetObject);
        }
    }

    public static class RField<Type> {
        private final String name;
        private final Object targetObject;
        private final Set<Field> possibleFields;

        public RField(final Set<Field> possibleFields, final Object targetObject) {
            requireNotEmpty(possibleFields);
            this.name = possibleFields.iterator().next().getName();
            this.targetObject = targetObject;
            this.possibleFields = possibleFields;
        }

        public Type get() {
            Field field = findSuitableField();
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return get(field);
        }

        @SuppressWarnings("unchecked")
        private Type get(Field field) {
            try {
                return (Type) field.get(targetObject);
            } catch (IllegalArgumentException e) {
                throw new ReflectException(e);
            } catch (IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }

        private Field findSuitableField() {
            return possibleFields.iterator().next();
        }

        public void set(Type value) {
            Field field = findSuitableField();
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            set(field, value);
        }

        private void set(Field field, Type value) {
            try {
                field.set(targetObject, value);
            } catch (IllegalArgumentException e) {
                throw new ReflectException(e);
            } catch (IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }
    }

    public static class RMethod<ReturnType> {
        private final String name;
        private final Object targetObject;
        private final Set<Method> possibleMethods;

        public RMethod(final Set<Method> possibleMethods,
                final Object targetObject) {
            requireNotEmpty(possibleMethods);
            this.name = possibleMethods.iterator().next().getName();
            this.targetObject = targetObject;
            this.possibleMethods = possibleMethods;
        }

        public ReturnType invoke(Object... args) {
            Method method = findSuitableMethod(args);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return invoke(method, args);
        }

        @SuppressWarnings("unchecked")
        private ReturnType invoke(Method method, Object... args) {
            try {
                return (ReturnType) method.invoke(targetObject, args);
            } catch (IllegalArgumentException e) {
                throw new ReflectException(e);
            } catch (IllegalAccessException e) {
                throw new ReflectException(e);
            } catch (InvocationTargetException e) {
                throw new ReflectException(e);
            }
        }

        private Method findSuitableMethod(final Object[] args) {
            final List<Class<?>> argTypes = new ArrayList<Class<?>>();
            for (Object arg : args) {
                argTypes.add(arg.getClass());
            }
            for (final Method m : possibleMethods) {
                if (areArgumentTypesCompatible(argTypes, m)) {
                    return m;
                }
            }
            throw new ReflectException("No method '" + name + "(" + argTypes
                    + ")" + "' in targetObject '" + targetObject
                    + "'. Methods tried: " + possibleMethods);
        }

        private boolean areArgumentTypesCompatible(
                final List<Class<?>> callParameterTypes, final Method m) {
            List<Class<?>> methodParameterTypes = Arrays.asList(m
                    .getParameterTypes());
            if (methodParameterTypes.size() != callParameterTypes.size()) {
                return false;
            }
            for (int i = 0; i < methodParameterTypes.size(); i++) {
                Class<?> methodParameterType = autoboxIfNecessary(methodParameterTypes
                        .get(i));
                Class<?> callParameterType = callParameterTypes.get(i);
                if (!methodParameterType.isAssignableFrom(callParameterType)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static Set<Method> allMethods(final Class<?> klass) {
        final Set<Method> allMethods = new HashSet<Method>();
        Class<?> currentLevelOfDeclarations = klass;
        while (currentLevelOfDeclarations != null) {
            allMethods.addAll(Arrays.asList(currentLevelOfDeclarations
                    .getDeclaredMethods()));
            currentLevelOfDeclarations = currentLevelOfDeclarations
                    .getSuperclass();
        }
        return allMethods;
    }

    private static Set<Field> allFields(final Class<?> klass) {
        final Set<Field> allFields = new HashSet<Field>();
        Class<?> currentLevelOfDeclarations = klass;
        while (currentLevelOfDeclarations != null) {
            allFields.addAll(Arrays.asList(currentLevelOfDeclarations
                    .getDeclaredFields()));
            currentLevelOfDeclarations = currentLevelOfDeclarations
                    .getSuperclass();
        }
        return allFields;
    }

    private static Class<?> autoboxIfNecessary(Class<?> klass) {
        if (klass.isPrimitive()) {
            return autobox(klass);
        }
        return klass;
    }

    private static Class<?> autobox(Class<?> klass) {
        if (Boolean.TYPE.equals(klass)) {
            return Boolean.class;
        }
        if (Byte.TYPE.equals(klass)) {
            return Byte.class;
        }
        if (Integer.TYPE.equals(klass)) {
            return Integer.class;
        }
        if (Long.TYPE.equals(klass)) {
            return Long.class;
        }
        if (Short.TYPE.equals(klass)) {
            return Short.class;
        }
        if (Float.TYPE.equals(klass)) {
            return Float.class;
        }
        if (Double.TYPE.equals(klass)) {
            return Double.class;
        }
        if (Character.TYPE.equals(klass)) {
            return Character.class;
        }
        throw new BugException("Tried to autobox class '" + klass
                + "' which is not autoboxable.");
    }

    private static void requireNotNull(final Object notNull,
            final String message) {
        if (notNull == null) {
            throw new ReflectException(message);
        }
    }

    private static void requireNotEmpty(final Collection<?> notEmpty) {
        if (notEmpty == null) {
            throw new ReflectException(
                    "Collection passed to requireNotEmpty was null.");
        }
        if (notEmpty.isEmpty()) {
            throw new ReflectException(
                    "Collection passed to requireNotEmpty was empty: "
                            + notEmpty);
        }
    }
}
