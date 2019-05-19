package de.slowloris.toaster.controller.connection;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements Connection {

    private String host;
    private int port;
    private Socket socket;

    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            socket = new Socket(host, port);
            write(new JSONObject().put("data", "connected"));
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean connect() {
        return false;
    }

    public boolean disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean write(Object o) {

        if(o instanceof JSONObject){
            try {
                OutputStream os = socket.getOutputStream();
                PrintStream ps = new PrintStream(os, true);
                ps.println(o.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return true;
            }
        }

        return false;
    }

    public Object read() {
        try {

            InputStream is = socket.getInputStream();
            BufferedReader buff = new BufferedReader(new InputStreamReader(is));

            String read = "";
            if(buff.ready()){
                read = buff.readLine();
            }

            if(read != ""){
                return read;
            }

        }catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
}
