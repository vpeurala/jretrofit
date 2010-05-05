package org.jretrofit;

public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = 1977L;

    public ReflectException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ReflectException(String arg0) {
        super(arg0);
    }

    public ReflectException(Throwable arg0) {
        super(arg0);
    }
}
