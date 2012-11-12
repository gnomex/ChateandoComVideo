package br.unioeste.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import br.unioeste.server.ServicesListener;
import br.unioeste.server.ServerMessenger;

import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldPacksReceived;
	private JTextField textFieldPacksSenders;
	private JTextField textFieldPacksLosts;

	private ServerMessenger sm;
	private ServicesListener serviceListener;
	
	private JTextArea textAreaStatus ;
	
	private JLabel labelClientsCounter;
	private JTextField textFieldBytesReceived;
	private JTextField textFieldBytesSent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
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
	public ServerGUI() {

		super("Services");

		try {	/**Pegar variaveis de ambiente*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		} catch (Exception e) {
			System.out.println("Erro ao obter variaveis de ambiente");
		}

		serviceListener = new Analysis();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 460);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 523, 58);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblTotalClientsConnecteds = new JLabel("Total Clients Connecteds :");
		lblTotalClientsConnecteds.setBounds(12, 24, 202, 22);
		panel.add(lblTotalClientsConnecteds);

		labelClientsCounter = new JLabel("0");
		labelClientsCounter.setBounds(226, 24, 168, 22);
		panel.add(labelClientsCounter);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				startServices();

			}
		});
		btnRun.setBounds(412, -1, 105, 25);
		panel.add(btnRun);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				sm.STOPSERVER();

			}
		});
		btnStop.setBounds(412, 33, 105, 25);
		panel.add(btnStop);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 82, 523, 338);
		contentPane.add(tabbedPane);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Status", null, panel_1, null);
		panel_1.setLayout(null);
		
		textAreaStatus = new JTextArea();
		textAreaStatus.setBounds(12, 12, 494, 287);
		panel_1.add(textAreaStatus);
		
		JScrollPane scrollPane = new JScrollPane(textAreaStatus);
		scrollPane.setBounds(12, 12, 494, 287);
		panel_1.add(scrollPane);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Packages", null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblRecebidos = new JLabel("Recebidos");
		lblRecebidos.setBounds(12, 32, 93, 15);
		panel_2.add(lblRecebidos);

		JLabel lblEnviados = new JLabel("Enviados");
		lblEnviados.setBounds(195, 32, 83, 15);
		panel_2.add(lblEnviados);

		JLabel lblPerdidos = new JLabel("Perdidos");
		lblPerdidos.setBounds(384, 32, 83, 15);
		panel_2.add(lblPerdidos);

		textFieldPacksReceived = new JTextField();
		textFieldPacksReceived.setText("0");
		textFieldPacksReceived.setBounds(12, 59, 124, 19);
		panel_2.add(textFieldPacksReceived);
		textFieldPacksReceived.setColumns(10);

		textFieldPacksSenders = new JTextField();
		textFieldPacksSenders.setText("0");
		textFieldPacksSenders.setBounds(186, 59, 124, 19);
		panel_2.add(textFieldPacksSenders);
		textFieldPacksSenders.setColumns(10);

		textFieldPacksLosts = new JTextField();
		textFieldPacksLosts.setText("0");
		textFieldPacksLosts.setBounds(384, 59, 114, 19);
		panel_2.add(textFieldPacksLosts);
		textFieldPacksLosts.setColumns(10);
		
		JLabel lblTotalDeBytes = new JLabel("Fluxo de dados");
		lblTotalDeBytes.setBounds(12, 139, 206, 15);
		panel_2.add(lblTotalDeBytes);
		
		textFieldBytesReceived = new JTextField();
		textFieldBytesReceived.setText("0");
		textFieldBytesReceived.setBounds(12, 166, 141, 19);
		panel_2.add(textFieldBytesReceived);
		textFieldBytesReceived.setColumns(10);
		
		JLabel lblBytesRecebidos = new JLabel("Bytes recebidos");
		lblBytesRecebidos.setBounds(171, 166, 139, 15);
		panel_2.add(lblBytesRecebidos);
		
		textFieldBytesSent = new JTextField();
		textFieldBytesSent.setText("0");
		textFieldBytesSent.setBounds(12, 216, 141, 19);
		panel_2.add(textFieldBytesSent);
		textFieldBytesSent.setColumns(10);
		
		JLabel lblBytesEnviados = new JLabel("Bytes enviados");
		lblBytesEnviados.setBounds(171, 218, 139, 15);
		panel_2.add(lblBytesEnviados);
	}

	public void startServices(){
		
		ExecutorService serverExecutor = Executors.newCachedThreadPool();
		serverExecutor.execute(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
            		sm = new ServerMessenger( serviceListener );
            		sm.startServer(); // start server
                } catch (Exception ex) {
                    
                }
            }
        }));

	}

	private class Analysis implements ServicesListener{

		@Override
		public void packetsReceived(int counter) {
			
			int old = Integer.parseInt(textFieldPacksReceived.getText());
			textFieldPacksReceived.setText(Integer.toString(counter + old));
		}

		@Override
		public void packetsSender(int counter) {
			int old = Integer.parseInt(textFieldPacksSenders.getText());
			textFieldPacksSenders.setText(Integer.toString(counter + old));

		}

		@Override
		public void packetLost(int counter) {
		
			int old = Integer.parseInt(textFieldPacksLosts.getText());
			textFieldPacksLosts.setText(Integer.toString(counter + old));

		}

		@Override
		public void registerLog(String status) {
			textAreaStatus.append("\n" + status);

		}

		@Override
		public void clientsCounter(int counter) {
			
			labelClientsCounter.setText(Integer.toString(counter));
			
		}

		@Override
		public void receiveData(float buffer) {
			
			float old = Float.parseFloat(textFieldBytesReceived.getText());
			textFieldPacksLosts.setText(Float.toString(buffer + old));
		}

		@Override
		public void sentData(float buffer) {
			
			float old = Float.parseFloat(textFieldBytesSent.getText());
			textFieldPacksSenders.setText(Float.toString(buffer + old));
			
		}

		@Override
		public void fileDatareceiver(float bufferin) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void fileDatasent(float bufferout) {
			// TODO Auto-generated method stub
			
		}

	}
}
