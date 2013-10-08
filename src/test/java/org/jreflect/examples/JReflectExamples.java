package org.jreflect.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Random;

import org.jretrofit.Reflect;
import org.junit.Test;

public class JReflectExamples {
    private final Random random = new Random();
    private final ExampleClass example = new ExampleClass();

    @SuppressWarnings("boxing")
    @Test
    public void primitiveIntArgument() {
        final int randomInt = random.nextInt();

        Reflect.on(example).method("methodWithPrimitiveIntParameter")
                .invoke(randomInt);
        assertEquals(randomInt,
                example.argumentOfMethodWithPrimitiveIntParameter);
    }

    @Test
    public void objectIntegerArgument() {
        final Integer randomInteger = new Integer(random.nextInt());
        Reflect.on(example).method("methodWithObjectIntegerParameter")
                .invoke(randomInteger);
        assertSame(randomInteger,
                example.argumentOfMethodWithObjectIntegerParameter);
    }
}
