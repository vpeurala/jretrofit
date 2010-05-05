package org.jretrofit.fixture;

public class Fixture {
    private final Person ville;

    public Fixture() {
        ville = new Person();
        ville.setAge(32);
        ville.setFavoriteColor("white");
        ville.setHome("koti");
        ville.setName("Ville");
    }

    public Person ville() {
        return ville;
    }
}
