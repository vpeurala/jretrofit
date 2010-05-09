package org.jretrofit;

public class BugException extends RuntimeException {
    private static final long serialVersionUID = 1977L;

    public static final String STANDARD_MESSAGE = "\n"
            + "This is an internal bug in JRetrofit, sorry for that."
            + "\n"
            + "Please report this bug and help make JRetrofit better."
            + "\n"
            + "It would be perfect if you could also provide a failing unit test case."
            + "\n" + "\n" + "http://github.com/vpeurala/jretrofit" + "\n"
            + "http://www.jretrofit.org" + "\n";

    public BugException(String message) {
        super(message + STANDARD_MESSAGE);
    }
}
