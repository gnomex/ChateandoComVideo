package br.unioeste.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.unioeste.client.SocketMessageManager;
import br.unioeste.client.User;
import br.unioeste.messenger.ManageMessages;
import br.unioeste.messenger.MessagesListener;
import static br.unioeste.global.SocketConstants.*;

public class ClientGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea messageArea; // displays messages
	private JTextArea inputArea; // inputs messages
	
	private JButton sendButton; // sends messages

	private User user;
	
	private ManageMessages messageManager; // communicates with server
	private MessagesListener messageListener; // receives incoming messages

	// ClientGUI constructor
	public ClientGUI( User client) 
	{       
		super( "Socket Chat" );

		try {	/**Pegar variaveis de ambiente*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) {
			System.out.println("Erro ao obter variaveis de ambiente");
		}

		//messageManager =  new SocketMessageManager( SERVER_ADDRESS );
		
		
		user = client;
		
		// create MyMessageListener for receiving messages
		messageListener = new MyMessageListener(); 

		messageArea = new JTextArea(); // displays messages
		messageArea.setEditable( false ); // disable editing
		messageArea.setWrapStyleWord( true ); // set wrap style to word
		messageArea.setLineWrap( true ); // enable line wrapping

		// put messageArea in JScrollPane to enable scrolling
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout( new BorderLayout( 10, 10 ) );
		messagePanel.add( new JScrollPane( messageArea ), 
				BorderLayout.CENTER );

		inputArea = new JTextArea( 4, 20 ); // for entering new messages
		inputArea.setWrapStyleWord( true ); // set wrap style to word
		inputArea.setLineWrap( true ); // enable line wrapping


		sendButton = new JButton( "Send" ); // create send button
		sendButton.addActionListener(
				new ActionListener() 
				{
					// send new message when user activates sendButton
					public void actionPerformed( ActionEvent event )
					{

						messageManager.sendMessage( user.getUserName(), user.getUserTag(), 	inputArea.getText() );
						inputArea.setText( "" ); // clear inputArea

					} 
				}
				);

		Box box = new Box( BoxLayout.X_AXIS ); // create new box for layout
		box.add( new JScrollPane( inputArea ) ); // add input area to box
		box.add( sendButton ); // add send button to box
		messagePanel.add( box, BorderLayout.SOUTH );
		getContentPane().add( messagePanel, BorderLayout.CENTER );

	} 

	// MyMessageListener listens for new messages from MessageManager and 
	// displays messages in messageArea using MessageDisplayer.
	private class MyMessageListener implements MessagesListener 
	{
		// when received, display new messages in messageArea
		public void messageReceived( String from,String to, String message ) 
		{
			// append message using MessageDisplayer
			SwingUtilities.invokeLater( 
					new MessageDisplayer( from, to, message ) );
		} // end method messageReceived
	} // end MyMessageListener inner class

	// Displays new message by appending message to JTextArea.  Should
	// be executed only in Event thread; modifies live Swing component
	private class MessageDisplayer implements Runnable
	{
		private String fromUser; // user from which message came
		private String messageBody; // body of message
		private String toUser; //user to send

		// MessageDisplayer constructor
		public MessageDisplayer( String from, String to, String body )
		{
			fromUser = from; // store originating user
			toUser = to;
			messageBody = body; // store message body
		} // end MessageDisplayer constructor

		// display new message in messageArea
		public void run() 
		{
			// append new message
			messageArea.append( "\n" + fromUser + "> <To "+toUser + ">: " + messageBody );   
		} // end method run      
	} // end MessageDisplayer inner class
}