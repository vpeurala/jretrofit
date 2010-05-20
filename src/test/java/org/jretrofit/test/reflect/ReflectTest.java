package org.jretrofit.test.reflect;

import junit.framework.TestCase;

import org.jretrofit.Reflect;
import org.jretrofit.fixture.ClassWithStaticMembers;
import org.jretrofit.fixture.Fixture;
import org.jretrofit.fixture.Person;

public class ReflectTest extends TestCase {
    private Fixture fixture;
    private Person ville;
    private Object result;

    @Override
    protected void setUp() throws Exception {
        fixture = new Fixture();
        ville = fixture.ville();
        ClassWithStaticMembers.reset();
    }

    @Override
    protected void tearDown() throws Exception {
        ClassWithStaticMembers.reset();
    }

    public void testGetterCanBeInvoked() {
        result = Reflect.on(ville).method("getAge").invoke();
        assertResult(new Integer(32));
    }

    public void testGetterCanBeInvokedWithReturnType() {
        result = Reflect.on(ville).method("getAge", Integer.class).invoke();
        assertResult(new Integer(32));
    }

    public void testSetterCanBeInvoked() {
        Reflect.on(ville).method("setAge").invoke(new Integer(31));
        assertEquals(31, ville.getAge());
    }
    
    public void testStaticMethodCanBeInvoked() {
        result = Reflect.on(ClassWithStaticMembers.class).method("staticMethod").invoke("foo");
        assertEquals(Boolean.TRUE, result);
    }
    
    public void testStaticMethodOfInstanceCanBeInvoked() {
        result = Reflect.on(new ClassWithStaticMembers()).method("staticMethod").invoke("foo");
        assertEquals(Boolean.TRUE, result);
    }

    public void testFieldValueCanBeGet() {
        result = Reflect.on(ville).field("age").get();
        assertResult(new Integer(32));
    }

    public void testFieldValueCanBeGetWithType() {
        result = Reflect.on(ville).field("age", Integer.class).get();
        assertResult(new Integer(32));
    }

    public void testFieldValueCanBeSet() {
        Reflect.on(ville).field("age").set(new Integer(31));
        assertEquals(31, ville.getAge());
    }

    public void testFieldValueCanBeSetWithType() {
        Reflect.on(ville).field("age", Integer.class).set(new Integer(31));
        assertEquals(31, ville.getAge());
    }

    public void testStaticFieldValueCanBeGet() {
        result = Reflect.on(ClassWithStaticMembers.class).field("staticField")
                .get();
        assertResult("foo");
    }

    public void testStaticFieldValueCanBeGetWithType() {
        result = Reflect.on(ClassWithStaticMembers.class).field("staticField",
                String.class).get();
        assertResult("foo");
    }

    public void testStaticFieldValueCanBeSet() {
        Reflect.on(ClassWithStaticMembers.class).field("staticField")
                .set("bar");
        assertEquals("bar", ClassWithStaticMembers.getStaticFieldValue());
    }

    public void testStaticFieldValueCanBeSetWithType() {
        Reflect.on(ClassWithStaticMembers.class).field("staticField",
                String.class).set("bar");
        assertEquals("bar", ClassWithStaticMembers.getStaticFieldValue());
    }

    private void assertResult(Object expected) {
        assertEquals(expected, result);
    }
}
