/*
 * Copyright 2006 Ville Peurala
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jretrofit.basic;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Appender;
import org.jretrofit.AllMethodsNotImplementedException;
import org.jretrofit.Retrofit;
import org.jretrofit.Retrofitter;
import org.jretrofit.fixture.AngryException;
import org.jretrofit.fixture.City;
import org.jretrofit.fixture.CompleteHuman;
import org.jretrofit.fixture.Human;
import org.jretrofit.fixture.Person;
import org.jretrofit.fixture.Resident;

/**
 * A set of base assertions for unit tests. Extend from this class and try
 * different ways of creating a retrofitted class (for example, with/without
 * caching).
 * 
 * @author Ville Peurala
 */
public abstract class AbstractRetrofitTestCase extends TestCase {
    private static void assertNotImplementedMethods(String[] methodNames,
            AllMethodsNotImplementedException e) {
        List<String> assertedMethodNamesAsList = Arrays.asList(methodNames);
        List<String> notImplementedMethodNamesAsList = new ArrayList<String>();
        Method[] notImplementedMethods = e.getNotImplementedMethods();
        for (int i = 0; i < notImplementedMethods.length; i++) {
            notImplementedMethodNamesAsList.add(notImplementedMethods[i]
                    .getName());
        }
        for (String currentAssertedMethodName : assertedMethodNamesAsList) {
            if (!notImplementedMethodNamesAsList
                    .contains(currentAssertedMethodName)) {
                fail("Assertion contained method named '"
                        + currentAssertedMethodName
                        + "', which was not in the AllMethodsNotImplementedException.");
            }
        }
        for (String currentNotImplementedMethodName : notImplementedMethodNamesAsList) {
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
        return createRetrofitter().partial(person, Human.class);
    }

    protected abstract Retrofitter createRetrofitter();

    @Override
    protected final void setUp() throws Exception {
        person = new Person();
        person.setName("Antti");
        person.setAge(54);
        person.setFavoriteColor("Green");
        person.setHome(new City("Pori"));
        human = createPartialHuman();
    }

    /**
     * Person implements Comparable, so human implements it too, although
     * interface Human does not extend from Comparable!
     */
    public final void testCanCastProxyToAnyInterfaceImplementedByTargetObject() {
        Comparable<?> comparable = (Comparable<?>) human;
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
        Comparable<?> localComparable = (Comparable<?>) retrofittedObject;
        // This idiotic assertion is here just because we have very
        // strict compiler settings in this project. The code would
        // not compile with unused local variable 'comparable',
        // so we have to use it in some way.
        assertSame(localHuman, localComparable);
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
        Object retrofittedObject = createRetrofitter().complete(
                new CompleteHuman(),
                new Class[] { Human.class, Comparable.class });
        assertEquals("White", ((Human) retrofittedObject).getFavoriteColor());
        assertEquals(1, ((Comparable<?>) retrofittedObject).compareTo(null));
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
        Human retrofittedHuman = createRetrofitter().complete(
                new CompleteHuman(), Human.class);
        assertEquals("White", retrofittedHuman.getFavoriteColor());
    }

    public final void testRuntimeExceptionsAreThrownNormally() throws Exception {
        try {
            human.irritate(100);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("FOOOOOOOOO!!!!!!!!!!!!", expected.getMessage());
        }
    }

    public final void testRetrofittingCanBeDoneWithPrivateStubs()
            throws Exception {
        HumanStub stub = new HumanStub();
        Human fromPrivateStub = createRetrofitter().partial(stub, Human.class);
        assertEquals("Pertti", fromPrivateStub.getName());
    }

    public final void testProxiesCanBeSerialized() throws Exception {
        Serializable wrappedObject = new SerializableStub();
        Human proxy = createRetrofitter().partial(wrappedObject, Human.class);
        byte[] serialized = serialize(proxy);
        Human deserialized = (Human) deserialize(serialized);
        assertEquals("Pena", deserialized.getName());
    }

    public final void testRetrofittingCanBeDoneWhenStubIsFromAHigherClassloaderThanInterfaceToImplement()
            throws Exception {
        createRetrofitter().partial(new Object(), Appender.class);
    }

    public final void testRetrofittingCanBeDoneWhenStubIsFromALowerClassloaderThanInterfaceToImplement() {
        createRetrofitter().partial(new HumanStub(), Comparable.class);
    }

    public final void testCannotPartiallyRetrofitANullTargetObject() {
        try {
            createRetrofitter().partial(null, Comparable.class);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Target object cannot be null!", expected.getMessage());
        }
    }

    public final void testCannotPartiallyRetrofitAnObjectWithANullInterface() {
        try {
            createRetrofitter().partial(new Object(), (Class<?>) null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Interface to implement cannot be null!", expected
                    .getMessage());
        }
    }

    public final void testCannotPartiallyRetrofitAnObjectWithANullArrayOfInterfaces() {
        try {
            createRetrofitter().partial(new Object(), (Class[]) null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Array of interfaces to implement cannot be null!",
                    expected.getMessage());
        }
    }

    public final void testCannotCompletelyRetrofitANullTargetObject() {
        try {
            createRetrofitter().complete(null, Comparable.class);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Target object cannot be null!", expected.getMessage());
        }
    }

    public final void testCannotCompletelyRetrofitAnObjectWithANullInterface() {
        try {
            createRetrofitter().complete(new Object(), (Class<?>) null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Interface to implement cannot be null!", expected
                    .getMessage());
        }
    }

    public final void testCannotCompletelyRetrofitAnObjectWithANullArrayOfInterfaces() {
        try {
            createRetrofitter().complete(new Object(), (Class[]) null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertEquals("Array of interfaces to implement cannot be null!",
                    expected.getMessage());
        }
    }

    public final void testCannotRetrofitIfNoSuitableClassloaderExists()
            throws Exception {
        try {
            createRetrofitter().partial(
                    new Object(),
                    new EvilClassLoader()
                            .loadClass("org.jretrofit.fixture.EvilFoo"));
            fail();
        } catch (RuntimeException expected) {
            String message = expected.getMessage();
            assertTrue(message
                    .startsWith("Could not find a suitable classloader for retrofitting!"));
        }
    }

    private byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private Object deserialize(byte[] serialized) throws IOException,
            ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                serialized);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        Object deserialized = objectInputStream.readObject();
        objectInputStream.close();
        return deserialized;
    }

    private static class SerializableStub implements Serializable {
        private static final long serialVersionUID = 1977L;
        public String getName() {
            return "Pena";
        }
    }

    private static class HumanStub {
        String getName() {
            return "Pertti";
        }
    }
}
