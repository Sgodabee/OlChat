 
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package chating.Client.chat;

/**
 *
 * @author Sgoda
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client  extends JFrame
{
    private JTextField txtMessage, txtStatus;
    private JTextArea taDisplay;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;
    private String message;
    ServerSocket server;
    private String ip;
    private JPanel pl1;
    private JPanel pl2;
    private JPanel pl3;
   private JButton btnSend;
    private  JLabel lbPicture;
   
    public Client(String host)
    {
        super ("Client Screen");
         ip = host;
         setLayout( new BorderLayout());
         
        txtMessage = new JTextField();
        txtMessage.setFont(new Font("Arial", Font.BOLD, 16));
        
        taDisplay = new JTextArea(10,2);
        taDisplay.setFont(new Font("Arial", Font.BOLD, 16));
        taDisplay.setEditable(false);
        btnSend =new JButton("Send");
        txtStatus = new JTextField(30);
        
        
       // lbPicture.setIcon(new ImageIcon((getClass().getResource("/magoda/chat/Client/IMG-20130701-WA0003.jpg"))));
        
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
        
        
        
        //add(txtMessage, BorderLayout.SOUTH);
        //add(new JScrollPane(taDisplay),BorderLayout.CENTER);
        setSize(500,700);
        setVisible(true);
        
    
    }
    
    public void startRunning()
    {
        try
        { 
            connectToserver();
            
            
        
        }
        catch (EOFException err)
        {
            showMessage("client ended connection");
        }
        catch(IOException hh)
        {
            hh.printStackTrace();
            
        }
        finally
        {
          // CloseChat();
            
        }
    
    }
    public void connectToserver() throws IOException
    {
        //"192.168.92.1"
        showMessage("checking connection \n ");
        socket = new Socket(InetAddress.getByName(ip),8000);
        showMessage(" connected to " + socket.getInetAddress().getHostName()+"\n" );
        setupStreams();
        
    }
     public void setupStreams()throws IOException
    {
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());
        showMessage ("\n Stream ready ");
        whileChatting();
       
    }
     
     public void whileChatting()throws IOException
    {
        
        ableTowrite(true);
        do
        { 
            try
            {
                message= (String ) input.readObject();  
                
                if(!(message.equals(null)))
                {
                    showMessage("\nAssitant >>-- " +message);
                }
            }
            catch ( ClassNotFoundException err)
            {
                showMessage("\n unable to send");
            }
           
       
        }while(!(message.equals("CLIENT -END")));
        
    }
     public void CloseChat()
    {
        showMessage("\n Close chat..");
        ableTowrite(false);
        try
        {
            output.close();
            input.close();
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
            output.writeObject(message);
            output.flush();
            //showMessage("\nCLIENT - "+ message);
        }
        
        catch(Exception err)
        {
            taDisplay.append("I can't send message");
            
        
        }
    
    }
    public void showMessage(final String txtMess)
    {
        SwingUtilities.invokeLater(
                new Runnable() {

            @Override
            public void run()
            {
                taDisplay.append(txtMess);
                taDisplay.setBackground(Color.WHITE);
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
    
    

