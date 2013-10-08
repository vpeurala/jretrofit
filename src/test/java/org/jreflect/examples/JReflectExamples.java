package org.jreflect.examples;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.jretrofit.Reflect;
import org.junit.Test;

public class JReflectExamples {
    private static class ExampleClass {
        public int argumentOfMethodWithPrimitiveIntParameter;

        public void methodWithPrimitiveIntParameter(int input) {
            argumentOfMethodWithPrimitiveIntParameter = input;
        }
    }

    private final Random random = new Random();
    private final ExampleClass example = new ExampleClass();

    @Test
    public void testSetterCanBeInvoked() {
        final int randomInt = random.nextInt();
        Reflect.on(example).method("methodWithPrimitiveIntParameter")
                .invoke(new Integer(randomInt));
        assertEquals(randomInt,
                example.argumentOfMethodWithPrimitiveIntParameter);
    }
}
