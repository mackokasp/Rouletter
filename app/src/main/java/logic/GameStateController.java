package logic;

import android.view.View;

import JSSON.*;
import communication_and_logic.ClientCommunicationThread;
import com.sourcey.materiallogindemo.TableActivity;
import com.sourcey.materiallogindemo.SignupActivity;

import java.io.IOException;

/**
 * Created by sackhorn on 16.05.17.
 */
public class GameStateController implements Runnable {
    ClientStates currentState;
    Overseer mainOverseer;
    SignupActivity connectGUI;
    TableActivity bettingGUI;
    public String clientLogin;
    public String clientPassword;

    public GameStateController(Overseer overseer) {
        currentState = ClientStates.UNCONNECTED;
        mainOverseer = overseer;
       bettingGUI=mainOverseer.bettingGUI;
       connectGUI=mainOverseer.connectGUI;
       // connectGUI.addLoginActionListener(new LoginActionListener());
       // connectGUI.addSignUpActionListener(new SignUpActionListener());

    }







    public void handleIncomingMessage(JSONMessage msg) throws InterruptedException {
        System.out.println("SERVER: " + msg.rawJSONString);
        switch(msg.getMsgType()){
            case SIGN_UP_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                break;

            case LOGIN_OK:
                this.clientLogin = connectGUI.getLogin();
                this.clientPassword = connectGUI.getPassword();
                changeLogToGameFrame();
                break;

            case LOGIN_DUPLICATE:
            //    JOptionPane.showMessageDialog(bettingGUI,
              //          "User with given login already exists");
                break;

            case BET_OK:
                bettingGUI.setBetResult(MessageType.BET_OK);
                break;
            case WRONG_PASS:
                //JOptionPane.showMessageDialog(bettingGUI,
                  //      "Wrong password for given Login.");
                connectGUI.clearPassword();
                break;
            case BLOCKED:
                //JOptionPane.showMessageDialog(bettingGUI,
                  //      "User blocked");
                break;
            case BET_UNABLE:
                bettingGUI.unlockBettingGUI();
                bettingGUI.setBetResult(MessageType.BET_UNABLE);
                //JOptionPane.showMessageDialog(bettingGUI,
                  //      "Unable to bet.");
                break;
            case BAD_SESSION_ID:
                bettingGUI.unlockBettingGUI();
                bettingGUI.setBetResult(MessageType.BAD_SESSION_ID);
                break;
            case TIMESTAMP_TO_BET:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_BET);
                bettingGUI.clearBetResult();
                bettingGUI.unlockBettingGUI();
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance"));
                //odblokować wpisywanie wartosci betu i przycisk betowania
                break;
            case TIMESTAMP_TO_RESULT:
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_RESULT);
                bettingGUI.clearBetResult();
                bettingGUI.setResultLabel(msg.getDictionary().get("result"));
                bettingGUI.setAccountLabel(msg.getDictionary().get("account_balance")); // nie wiem czemu tu nie wyciaga nic
                //wyswietlić wynik losowania
                break;
            case TIMESTAMP_TO_ROLL:
                bettingGUI.lockBettingGUI();
                bettingGUI.clearBetResult();
                bettingGUI.setGameStateInfoLabel(MessageType.TIMESTAMP_TO_ROLL);
                //zablokowac wpisywanie wartosci betu i przycisk betowania + Licznik
                break;
            default:
                break;
        }
    }

    /**
     *  Called only from GUI, semaphore acquire in listener
     */
    public boolean sendMessage(JSONMessage msg) throws IOException, InterruptedException {
        mainOverseer.communicationThread.sendMessage(msg);
        return true;
    }
    public void connect(String host, int port){
        ClientCommunicationThread comThread = connectToServer(host, port);
        mainOverseer.communicationThread = comThread;
       System.out.println(host);
        assert comThread != null;
        try {
              //comThread.join();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ClientCommunicationThread connectToServer(String ip, int port) {
        ClientCommunicationThread newCommunicationThread;
        try
        {
            newCommunicationThread = new ClientCommunicationThread(ip,port);
            newCommunicationThread.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Unable to connect");
            return null;
        }
        return newCommunicationThread;
    }
    private void changeLogToGameFrame(){
       connectGUI.changeLogToGameFrame();


    }

    @Override
    public void run() {
        connect("127.0.0.1",1234);
        System.out.println("GO");
    }
}