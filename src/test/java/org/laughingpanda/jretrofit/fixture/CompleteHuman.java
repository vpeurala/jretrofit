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

import java.util.Date;

/**
 * @author Ville Peurala
 */
public class CompleteHuman {
    private Object parameterOfLastCall;

    public int getAge(Date onDate) {
        parameterOfLastCall = onDate;
        return 0;
    }

    public String getFavoriteColor() {
        return "White";
    }

    public String getHome() {
        return null;
    }

    public String getName() {
        return null;
    }

    public void irritate(int howBadly) {
        parameterOfLastCall = new Integer(howBadly);
    }

    public void setFavoriteColor(Object color) {
        parameterOfLastCall = color;
    }

    public void setHome(String home) {
        parameterOfLastCall = home;
    }

    public Object getParameterOfLastCall() {
        return parameterOfLastCall;
    }

    public int compareTo(Object o) {
        return (o == this) ? -1 : 1;
    }
}
