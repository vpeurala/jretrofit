package net.sf.jretrofit.fixture;

/**
 * @author Ville Peurala
 */
public class City {
    private final String name;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "City: " + name + ".";
    }
}
