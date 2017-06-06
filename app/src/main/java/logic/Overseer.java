package logic;

import JSSON.JSONMessage;
import JSSON.JSONMessageBuilder;
import JSSON.MessageType;
import communication_and_logic.ClientCommunicationThread;
import com.sourcey.materiallogindemo.TableActivity;
//import view.ConnectGUI;
import com.sourcey.materiallogindemo.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Created by sackhorn on 16.05.17.
 */
public class Overseer {
    private static Overseer Instance;

    public boolean isRunningFlag = true;
    public boolean listenFlag = true; //TODO: The listenFlag May not be necessary since we are using semaphores but i will let i stay for now
    public Semaphore comFlagSemaphore = new Semaphore(1);
    public GameStateController gameStateController = new GameStateController(this);
    public ClientCommunicationThread communicationThread;
    public TableActivity bettingGUI;
    public SignupActivity connectGUI=SignupActivity.getInstance();





    private   Overseer(){
        bettingGUI=TableActivity.getInstance() ;
        connectGUI=SignupActivity.getInstance();


    }

    public static Overseer getInstance()
    {
        if(Instance==null)
            Instance = new Overseer();
        return Instance;
    }

     public   void toast()
     {connectGUI.makeToast();}





}
