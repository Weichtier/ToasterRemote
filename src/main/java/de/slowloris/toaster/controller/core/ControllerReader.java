package de.slowloris.toaster.controller.core;

import com.github.strikerx3.jxinput.*;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
import de.slowloris.toaster.controller.gui.MainGUI;
import org.json.JSONObject;

public class ControllerReader {


    private boolean leftButtonPressed = false;
    private boolean upButtonPressed = false;
    private int readLOld;
    private int readROld;

    public boolean isAvailable(){
        return XInputDevice14.isAvailable();
    }

    public XInputDevice14 getDevice(){
        if(XInputDevice14.isAvailable()){
            try {
                return XInputDevice14.getDeviceFor(0);
            } catch (XInputNotLoadedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void read(){
        try {

            getDevice().poll();

            XInputComponents components = getDevice().getComponents();

            XInputButtons buttons = components.getButtons();
            XInputAxes axes = components.getAxes();

            if(buttons.left){
                if(!leftButtonPressed){
                    leftButtonPressed = true;
                    MainGUI.getInstance().sensitiveCheckBox.setSelected(!MainGUI.getInstance().sensitiveCheckBox.isSelected());
                }
            }else {
                if(leftButtonPressed) leftButtonPressed = false;
            }

            if(buttons.up){
                if(!upButtonPressed){
                    upButtonPressed = true;
                    if(MainGUI.getInstance().gearLabel.getText().equalsIgnoreCase("Gear: Forward")){
                        MainGUI.getInstance().gearLabel.setText("Gear: Backward");
                        Main.getConnection().write(new JSONObject().put("data", "changeGear").put("gear", -1));
                    }else if(MainGUI.getInstance().gearLabel.getText().equalsIgnoreCase("Gear: Backward")){
                        MainGUI.getInstance().gearLabel.setText("Gear: Forward");
                        Main.getConnection().write(new JSONObject().put("data", "changeGear").put("gear", 1));
                    }
                }
            }else {
                if(upButtonPressed) upButtonPressed = false;
            }

            int readL = axes.ltRaw;
            int readR = axes.rtRaw;

            if(MainGUI.getInstance().sensitiveCheckBox.isSelected()){
                readL = readL / 3;
                readR = readR / 3;
            }

            if(readLOld != readL || readROld != readR){
                JSONObject obj = new JSONObject();
                obj.put("data", "updateSpeed");
                obj.put("speed", new JSONObject().put("l", readL).put("r", readR));
                Main.getConnection().write(obj);
                MainGUI.getInstance().inR.setValue(readR);
                MainGUI.getInstance().inL.setValue(readL);
                MainGUI.getInstance().speedLabel.setText(readL + " | " + readR);

                readLOld = readL;
                readROld = readR;
            }

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
