package chating.Client.chat;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sgoda
 */
public class Server extends JFrame
{
    private JTextField txtMessage,txtStatus;
    private JTextArea taDisplay;
    private ObjectOutputStream writeToClient;
    private ObjectInputStream readFromClient;
    private Socket socket;
    private String mess;
    ServerSocket server;
    private JButton btnSend;
    private  JLabel lbPicture;
    private JPanel pl1;
    private JPanel pl2;
    private JPanel pl3;

    public Server() 
    {
        super("Assistant ");
        txtMessage = new JTextField();
        taDisplay = new JTextArea(10,2);
        taDisplay.setEditable(false);
         btnSend =new JButton("Send");
        txtStatus = new JTextField(30);
        
        
          pl1 = new JPanel(new GridLayout(5,2));
         pl2 = new JPanel(new BorderLayout());
         pl3 = new JPanel(new GridLayout(1,2));
         lbPicture = new JLabel();
        
        txtMessage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                sendMessage(e.getActionCommand());
                txtMessage.setText("");
                
                
            }
        });

        txtStatus.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                
                status(e.getActionCommand());
                txtStatus.setText("");
                
            }
        });
        
        
        pl1.add(lbPicture);
        pl1.add(txtStatus);
         pl2.add(taDisplay);
         pl3.add(txtMessage);
         pl3.add(btnSend);
         
        add(pl1,BorderLayout.NORTH);
        add(pl2,BorderLayout.CENTER);
        add(pl3,BorderLayout.SOUTH);
       
        setSize(500,700);
        setVisible(true);
 
    }
 
    public void startRunning()
    {
         try{
        server = new ServerSocket(8000);
        
      //  socket =server.accept();
            while(true)
            {   
                    try
                    {
                        waitForConnection();
                        setupStreams();
                        whileChatting();
                   
                    }
                    
                 catch (EOFException err)
            {
                mess("\n client ended connection \n");
            }
           
                
            }
    
        }
          catch(IOException hh)
            {
                hh.printStackTrace();

            } 
        
       
         finally
         {
             
         
         }
    }
  
public void waitForConnection() throws IOException
    {
        mess ("waiting for Student....\n");
        socket = server.accept();
        mess("now Connected "+socket.getInetAddress().getHostName());
    
    }
    public void setupStreams()throws IOException
    {
        writeToClient = new ObjectOutputStream(socket.getOutputStream());
        writeToClient.flush();
        readFromClient = new ObjectInputStream(socket.getInputStream());
        mess ("\n Ready for conversation ");
       
    }
    public void whileChatting()throws IOException
    {
        String message = " \n you are connected";
        sendMessage(message);
        ableTowrite(true);
        do
        { 
            try
            {
                message= (String ) readFromClient.readObject();
                if(!(message.equals(null)))
                {
                    sendMessage("\nStudent >>-- " +message);
                }
                
            }
            catch ( ClassNotFoundException err)
            {
                mess("\n unable to send");
            }
            
        }while(!(message.equals("SERVER - END")));
        
    }
    public void CloseChat()
    {
        sendMessage("\n Close connection..\n");
        ableTowrite(false);
        try
        {
            writeToClient.close();
            readFromClient.close();
            socket.close();
            
        }
        catch(IOException err)
        {
            err.printStackTrace();
            
        } 
        
    }
    
    public void sendMessage(String message)
    {
        try
        {
            writeToClient.writeObject(message);
            writeToClient.flush();
            mess("\n Assitant - "+ message);
        }
        
        catch(IOException err)
        {
            taDisplay.append("I can send message");
            
        
        }
    
    }
    public void mess(final String txtMess)
    {
        SwingUtilities.invokeLater(
                new Runnable() {

            @Override
            public void run()
            {
                taDisplay.append(txtMess);
            }
        }
     );

    }     
   public void status(final String status)
    {
          SwingUtilities.invokeLater(
                new Runnable() {

            @Override
            public void run()
            {
                
                lbPicture.setText("Current Status->> "+status);
            }
        }
     );
    
    
    }   
    
    
    
    
      public void ableTowrite(final boolean tof)
    {
        SwingUtilities.invokeLater(
                new Runnable() {

            @Override
            public void run()
            {
              taDisplay.setEditable(tof);
            }
        }
     );

    }    
    
    
    
    
    
    
}
