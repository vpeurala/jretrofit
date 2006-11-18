package org.laughingpanda.jretrofit;

import java.awt.Color;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.laughingpanda.jretrofit.fixture.AngryException;
import org.laughingpanda.jretrofit.fixture.City;
import org.laughingpanda.jretrofit.fixture.CompleteHuman;
import org.laughingpanda.jretrofit.fixture.Human;
import org.laughingpanda.jretrofit.fixture.Person;
import org.laughingpanda.jretrofit.fixture.Resident;

/**
 * A set of base assertions for unit tests. Extend
 * from this class and try different ways of 
 * creating a retrofitted class (for example, 
 * with/without caching).
 * 
 * @author Ville Peurala
 */
public abstract class AbstractRetrofitTestCase extends TestCase {
    private static void assertNotImplementedMethods(String[] methodNames,
            AllMethodsNotImplementedException e) {
        List assertedMethodNamesAsList = Arrays.asList(methodNames);
        List notImplementedMethodNamesAsList = new ArrayList();
        Method[] notImplementedMethods = e.getNotImplementedMethods();
        for (int i = 0; i < notImplementedMethods.length; i++) {
            notImplementedMethodNamesAsList.add(notImplementedMethods[i]
                    .getName());
        }
        for (Iterator it = assertedMethodNamesAsList.iterator(); it.hasNext();) {
            String currentAssertedMethodName = (String) it.next();
            if (!notImplementedMethodNamesAsList
                    .contains(currentAssertedMethodName)) {
                fail("Assertion contained method named '"
                        + currentAssertedMethodName
                        + "', which was not in the AllMethodsNotImplementedException.");
            }
        }
        for (Iterator it = notImplementedMethodNamesAsList.iterator(); it
                .hasNext();) {
            String currentNotImplementedMethodName = (String) it.next();
            if (!assertedMethodNamesAsList
                    .contains(currentNotImplementedMethodName)) {
                fail("AllMethodsNotImplementedException contained method named '"
                        + currentNotImplementedMethodName
                        + "', which was not given in the assertion.");
            }
        }
    }
    protected Human human;

    protected Person person;

    protected final Human createPartialHuman() {
        return (Human) createRetrofitter().partial(person, Human.class);
    }

    protected abstract Retrofitter createRetrofitter();

    protected final void setUp() throws Exception {
        person = new Person();
        person.setName("Antti");
        person.setAge(54);
        person.setFavoriteColor("Green");
        person.setHome(new City("Pori"));
        human = createPartialHuman();
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
        Object retrofittedObject = Retrofit.partial(person, new Class[] {
                Human.class, Resident.class });
        Human localHuman = (Human) retrofittedObject;
        assertEquals("Antti", localHuman.getName());
        Resident localResident = (Resident) retrofittedObject;
        assertEquals("City: Pori.", localResident.getHomeAddress());
        Comparable localComparable = (Comparable) retrofittedObject;
        assertEquals(1, localComparable.compareTo(this));
    }

    public final void testCanInvokeAMethodWithParameterOfNarrowerType() {
        human.setHome("Ilmajoki");
        assertEquals("Ilmajoki", person.getHome());
    }

    public final void testCanInvokeAMethodWithReturnValueOfNarrowerType() {
        assertEquals(person.getFavoriteColor(), human.getFavoriteColor());
    }

    public final void testCanInvokeAnIdenticalMethod() {
        assertEquals(person.getName(), human.getName());
    }

    public final void testCannotInvokeAMethodWithParameterOfWiderType() {
        try {
            human.setFavoriteColor(Color.blue);
            fail();
        } catch (UnsupportedOperationException expected) {}
    }

    public final void testCannotInvokeAMethodWithReturnValueOfWiderType() {
        try {
            human.getHome();
            fail();
        } catch (UnsupportedOperationException expected) {}
    }

    public final void testCannotInvokeAMethodWithWrongNumberOfParameters() {
        try {
            human.getAge(new Date());
            fail();
        } catch (UnsupportedOperationException expected) {}
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

    public final void testCompleteRetrofittingOfHumanAndComparableWorksOnCompleteHuman() {
        createRetrofitter().complete(new CompleteHuman(),
                new Class[] { Human.class, Comparable.class });
    }

    public final void testCompleteRetrofittingOfHumanDoesNotWorkOnPerson() {
        try {
            createRetrofitter().complete(person, Human.class);
        } catch (AllMethodsNotImplementedException e) {
            assertNotImplementedMethods(new String[] { "setFavoriteColor",
                    "getAge", "getHome" }, e);
        }
    }

    public final void testCompleteRetrofittingOfHumanWorksOnCompleteHuman() {
        createRetrofitter().complete(new CompleteHuman(), Human.class);
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
