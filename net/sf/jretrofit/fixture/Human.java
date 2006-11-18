package net.sf.jretrofit.fixture;

import java.util.Date;

/**
 * @author Ville Peurala
 */
public interface Human {
    public String getName();

    public int getAge(Date onDate);

    public Object getFavoriteColor();

    public void setFavoriteColor(Object color);

    public String getHome();

    public void setHome(String home);

    public void irritate(int howBadly) throws AngryException;
}
