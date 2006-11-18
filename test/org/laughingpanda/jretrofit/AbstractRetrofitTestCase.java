package org.laughingpanda.jretrofit;

import java.awt.Color;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;

import org.laughingpanda.jretrofit.Retrofit;
import org.laughingpanda.jretrofit.fixture.AngryException;
import org.laughingpanda.jretrofit.fixture.City;
import org.laughingpanda.jretrofit.fixture.Human;
import org.laughingpanda.jretrofit.fixture.Person;
import org.laughingpanda.jretrofit.fixture.Resident;

import junit.framework.TestCase;

/**
 * A set of base assertions for unit tests. Extend
 * from this class and try different ways of 
 * creating a retrofitted class (for example, 
 * with/without caching).
 * 
 * @author Ville Peurala
 */
public abstract class AbstractRetrofitTestCase extends TestCase {
    protected Person person;
    protected Human human;

    protected final void setUp() throws Exception {
        person = new Person();
        person.setName("Antti");
        person.setAge(54);
        person.setFavoriteColor("Green");
        person.setHome(new City("Pori"));
        human = createHuman();
    }

    /**
     * In this method you must create a {@link Human} type object
     * which is actually a retrofitted {@link Person} object (the
     * same object which variable <code>person</person> in this
     * class refers to. For example:
     * 
     * <code>
     * return (Human) Retrofit.retrofit(person, Human.class);
     * </code>
     * 
     * @return a Person retrofitted to Human interface.
     */
    protected abstract Human createHuman();

    public final void testCanInvokeAnIdenticalMethod() {
        assertEquals(person.getName(), human.getName());
    }

    public final void testCannotInvokeAMethodWithWrongNumberOfParameters() {
        try {
            human.getAge(new Date());
            fail();
        } catch (UnsupportedOperationException expected) {}
    }

    public final void testCannotInvokeAMethodWithParameterOfWiderType() {
        try {
            human.setFavoriteColor(Color.blue);
            fail();
        } catch (UnsupportedOperationException expected) {}
    }

    public final void testCanInvokeAMethodWithParameterOfNarrowerType() {
        human.setHome("Ilmajoki");
        assertEquals("Ilmajoki", person.getHome());
    }

    public final void testCannotInvokeAMethodWithReturnValueOfWiderType() {
        try {
            human.getHome();
            fail();
        } catch (UnsupportedOperationException expected) {}
    }

    public final void testCanInvokeAMethodWithReturnValueOfNarrowerType() {
        assertEquals(person.getFavoriteColor(), human.getFavoriteColor());
    }

    /**
     * Person implements Comparable, so human implements it too,
     * although interface Human does not extend from Comparable!
     */
    public final void testCanCastProxyToAnyInterfaceImplementedByTargetObject() {
        Comparable comparable = (Comparable) human;
        // This idiotic assertion is here just because we have very 
        // strict compiler settings in this project. The code would
        // not compile with unused local variable 'comparable',
        // so we have to use it in some way.
        assertSame(human, comparable);
    }

    public final void testCanImplementMultipleInterfaces() {
        Object retrofittedObject = Retrofit.retrofit(person, new Class[] {
                Human.class, Resident.class });
        Human localHuman = (Human) retrofittedObject;
        assertEquals("Antti", localHuman.getName());
        Resident localResident = (Resident) retrofittedObject;
        assertEquals("City: Pori.", localResident.getHomeAddress());
        Comparable localComparable = (Comparable) retrofittedObject;
        assertEquals(1, localComparable.compareTo(this));
    }

    public final void testCheckedExceptionsWhichAreDeclaredOnTheInterfaceAreThrownNormally() {
        try {
            human.irritate(1);
            fail();
        } catch (AngryException expected) {
            assertEquals("Foo!", expected.getMessage());
        }
    }

    public final void testCheckedExceptionsWhichAreNotDeclaredOnTheInterfaceAreWrappedInUndeclaredThrowableExceptions()
            throws Exception {
        try {
            human.irritate(10);
            fail();
        } catch (UndeclaredThrowableException expected) {
            Throwable cause = expected.getUndeclaredThrowable();
            assertEquals(IllegalAccessException.class, cause.getClass());
            assertEquals("FOO!!", cause.getMessage());
        }
    }

    public final void testRuntimeExceptionsAreThrownNormally() throws Exception {
        try {
            human.irritate(100);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("FOOOOOOOOO!!!!!!!!!!!!", expected.getMessage());
        }
    }
}
