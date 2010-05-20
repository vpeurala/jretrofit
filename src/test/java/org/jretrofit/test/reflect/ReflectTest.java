package org.jretrofit.test.reflect;

import junit.framework.TestCase;

import org.jretrofit.Reflect;
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

    private void assertResult(Object expected) {
        assertEquals(expected, result);
    }
}
