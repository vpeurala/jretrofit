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
