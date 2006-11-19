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
package org.laughingpanda.jretrofit.fixture;

/**
 * @author Ville Peurala
 */
public class Person implements Comparable {
    private String name;
    private String favoriteColor;
    private Object home;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public Object getHome() {
        return home;
    }

    public void setHome(Object home) {
        this.home = home;
    }

    public int compareTo(Object o) {
        return 1;
    }

    public void irritate(int howBadly) throws AngryException,
            IllegalAccessException {
        if (howBadly <= 1) {
            throw new AngryException("Foo!");
        } else if (howBadly <= 10) {
            throw new IllegalAccessException("FOO!!");
        } else {
            throw new NullPointerException("FOOOOOOOOO!!!!!!!!!!!!");
        }
    }

    public String getHomeAddress() {
        return home.toString();
    }
}
