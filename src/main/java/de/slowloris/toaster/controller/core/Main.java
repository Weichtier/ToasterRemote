package de.slowloris.toaster.controller.core;

import de.slowloris.toaster.controller.configuration.Configuration;
import de.slowloris.toaster.controller.configuration.ConfigurationProvider;
import de.slowloris.toaster.controller.configuration.YamlConfiguration;
import de.slowloris.toaster.controller.connection.SocketConnection;
import de.slowloris.toaster.controller.gui.MainGUI;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Main {

    private static ControllerReader reader;
    private static SocketConnection connection;
    private static boolean running = true;
    private static File configFile;
    private static Configuration configuration;
    private static boolean wait;
    private static JFrame frame;

    public static void main(String[] args){

        configFile = new File("configuration.yml");

        if(!configFile.exists()){
            try {
                configFile.createNewFile();
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
                configuration.set("Socket.Hostname", "localhost");
                configuration.set("Socket.Port", 8088);

                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);

                System.out.println("PLEASE EDIT CONFIG AND RESTART");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainGUI.init();

        frame = new JFrame("Toaster Controller");
        frame.setSize(1080, 720);
        MainGUI.getInstance().panel.setSize(frame.getSize());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(MainGUI.getInstance().panel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                getConnection().disconnect();
                running = false;
            }
        });

        reader = new ControllerReader();
        connection = new SocketConnection(Main.getConfiguration().getString("Socket.Hostname"), Main.getConfiguration().getInt("Socket.Port"));

        while (running){
            reader.read();
            Object in = connection.read();
            if(in != null){
                JSONObject obj = new JSONObject(in.toString());
                System.out.println(obj.toString());
                if(obj.get("data").equals("close")){
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    JOptionPane.showMessageDialog(null, "Window closed by Server");
                }
            }
        }

    }

    public static SocketConnection getConnection() {
        return connection;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
