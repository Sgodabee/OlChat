/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servermain;

import chating.Client.chat.Client;
import chating.Client.chat.Server;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Sgoda
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        
         Object [] dicide ={"Host","Client"};
            String initSelect = "Host";
            
             Object select = JOptionPane.showInputDialog(null, "Login as :","Please Select", JOptionPane.QUESTION_MESSAGE,null,dicide,initSelect);
         
            if (select.equals("Host"))
            {
                 Server server = new Server();
                server.setVisible(true);
               server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                server.startRunning();
                
            }
            
            else if(select.equals("Client"))
            {
                
                String ipServer = JOptionPane.showInputDialog(null,"enter IP Address");
                Client client = new Client(ipServer);
               client.setVisible(true);
               client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                client.startRunning();
                
                
            }
    }
}
