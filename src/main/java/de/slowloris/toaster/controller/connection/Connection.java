package de.slowloris.toaster.controller.connection;

public interface Connection {
    boolean connect();
    boolean disconnect();
    boolean write(Object o);
    Object read();
}
