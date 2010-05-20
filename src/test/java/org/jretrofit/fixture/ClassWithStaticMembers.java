package org.jretrofit.fixture;

public class ClassWithStaticMembers {
    private static String staticField = "foo";

    private static boolean staticMethod(String s) {
        return s.equals("foo");
    }
    
    public static void reset() {
        staticField = "foo";
    }
    
    public static String getStaticFieldValue() {
        return staticField;
    }
}
