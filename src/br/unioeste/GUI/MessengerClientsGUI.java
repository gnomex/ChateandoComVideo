package br.unioeste.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.unioeste.client.SocketClientsManage;
import br.unioeste.client.SocketMessageManager;
import br.unioeste.client.User;
import br.unioeste.messenger.ClientListener;
import br.unioeste.messenger.ClientsList;
import br.unioeste.messenger.ManageClients;
import br.unioeste.messenger.ManageMessages;
import br.unioeste.messenger.MessagesListener;

import javax.swing.border.EtchedBorder;
import javax.swing.JMenuBar;

import static br.unioeste.global.SocketConstants.*;

public class MessengerClientsGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private DefaultListModel<String> model;

	private ManageMessages messageManager; // communicates with message server
	private ManageClients clientsManager; // communication with clients server

	private ClientListener clientsListener; //
	private MessagesListener messageListener;
	
	private User user;
	
	private JMenuItem connectMenuItem;
	private JMenuItem disconetMenuItem;
	private JMenuItem refreshMenuItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ManageMessages messageManager; // declare MessageManager
					messageManager = new SocketMessageManager( SERVER_ADDRESS );

					ManageClients clientsManager;
					clientsManager = new SocketClientsManage();

					MessengerClientsGUI frame = new MessengerClientsGUI(messageManager , clientsManager);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MessengerClientsGUI( ManageMessages managemessages , ManageClients clientsmanager) {

		try {	/**Pegar variaveis de ambiente*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) {
			System.out.println("Erro ao obter variaveis de ambiente");
		}


		messageManager = managemessages;
		clientsManager = clientsmanager;
		
		clientsListener = new MyClientListener();
		messageListener = new MyMessageListener();

		setTitle("Clients List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 735);

		JMenu serverMenu = new JMenu ( "Server" );
		JMenu serverChat = new JMenu ( "Chat" );


		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add( serverMenu ); // add server menu to menu bar
		menuBar.add(serverChat);
		setJMenuBar( menuBar ); // add JMenuBar to application

		ConnectListener connectionListener = new ConnectListener();
		
		connectMenuItem = new JMenuItem( "Connect");
		connectMenuItem.addActionListener(connectionListener);
		
		disconetMenuItem = new JMenuItem("Disconect");
		
		refreshMenuItem = new JMenuItem("Refresh");
		refreshMenuItem.setEnabled(false);
		refreshMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientsManager.getClientsList(clientsListener);
			}
		});
		

		JMenuItem ChatMenuItem = new JMenuItem("Chat em grupo");
		ChatMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Create a new ManageMessage	

				// create GUI for SocketMessageManager
				ClientGUI clientGUI = new ClientGUI( messageManager , "all" );
				clientGUI.setSize(500, 600);
				clientGUI.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				clientGUI.setLocationRelativeTo(null);
				clientGUI.setVisible( true ); // show window


			}
		});

		serverMenu.add(connectMenuItem);
		serverMenu.add(disconetMenuItem);
		serverMenu.add(refreshMenuItem);
		
		disconetMenuItem.setEnabled(false);

		serverChat.add(ChatMenuItem);

		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 566, 688);
		contentPane.add(panel);

		panel.setLayout(null);

		model = new DefaultListModel<String>();

		JList list = new JList(model);
		list.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		list.setBounds(24, 12, 518, 629);
		panel.add(list);

		list.setSelectionBackground(Color.ORANGE);


		ListDataListener listDataListener = new ListDataListener() {
			public void contentsChanged(ListDataEvent listDataEvent) {
				appendEvent(listDataEvent);
			}

			public void intervalAdded(ListDataEvent listDataEvent) {
				appendEvent(listDataEvent);
			}

			public void intervalRemoved(ListDataEvent listDataEvent) {
				appendEvent(listDataEvent);
			}

			private void appendEvent(ListDataEvent listDataEvent) {
				switch (listDataEvent.getType()) {
				case ListDataEvent.CONTENTS_CHANGED:
					System.out.println("Type: Contents Changed");
					break;
				case ListDataEvent.INTERVAL_ADDED:
					System.out.println("Novo cliente conectado");
					break;
				case ListDataEvent.INTERVAL_REMOVED:
					System.out.println("Type: Interval Removed");
					break;
				}
				DefaultListModel theModel = (DefaultListModel) listDataEvent.getSource();
				System.out.println(theModel);
			}
		};

		model.addListDataListener(listDataListener);

		NovaConversa novaConversa = new NovaConversa();

		list.addMouseListener(novaConversa);

	}

	private class MyClientListener implements ClientListener{

		public void clientreceived(User client) {
			// TODO Auto-generated method stub

		}

		public void clientListReceiver(ClientsList clientList) {
			SwingUtilities.invokeLater(new ClientsListUpdate(clientList.getClients()));

		}

	}

	private class ClientsListUpdate implements Runnable{

		private ArrayList<User> usersConnecteds;

		public ClientsListUpdate(ArrayList<User> users){
			usersConnecteds = users;
		}

		@Override
		public void run() {

			try{

				model.clear();
				if(!usersConnecteds.isEmpty()){


					for(User ur : usersConnecteds){
						model.addElement(ur.getUserName());
					}
				}else{
					model.addElement("Ninguem Conectado");
				}

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}


		}

	}

	private class ConnectListener implements ActionListener 
	{
		// connect to server and enable/disable GUI components
		public void actionPerformed( ActionEvent event )
		{

			// prompt for userName
			String userName = JOptionPane.showInputDialog( 
					MessengerClientsGUI.this, "Enter user name:" );
			
			user = new User();
			user.setUserName(userName);;
			user.setUserTag(userName);

			// connect to server and route messages to messageListener
			messageManager.connect(messageListener, user);

			clientsManager.addClient(user);
			clientsManager.getClientsList(clientsListener);
			
			connectMenuItem.setEnabled(false);
			disconetMenuItem.setEnabled(true);
			refreshMenuItem.setEnabled(true);
			
		} // end method actionPerformed      
	} // end ConnectListener inner class
	
	private class MyMessageListener implements MessagesListener 
	{
		public void messageReceived( String from,String to, String message ) 
		{

		} 
	}
	
	private class NovaConversa implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			JList theList = (JList) mouseEvent.getSource();
			if (mouseEvent.getClickCount() == 2) {
				int index = theList.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {
					Object o = theList.getModel().getElementAt(index);
					System.out.println("Solicitando nova conversa com: " + o.toString());
					try{

						// create GUI for SocketMessageManager
						ClientGUI clientGUI = new ClientGUI( messageManager , "" );
						clientGUI.setSize(500, 600);
						clientGUI.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
						clientGUI.setLocationRelativeTo(null);
						clientGUI.setVisible( true ); // show window


					}catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
		}


		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}



