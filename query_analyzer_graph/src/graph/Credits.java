package graph;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextPane;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;

public class Credits extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8492987621634472466L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Credits frame = new Credits();
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
	public Credits() {
		setTitle("Credits");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Credits.class.getResource("/images/logo.png")));
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //chiude tutti i frame, anche quello principale
		
		//dimensione dello schermo per centrare la finestra
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();	
		setBounds((width.intValue()/2)-187, (height.intValue()/2)-100, 300, 140);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTpchAnlyzer = new JLabel("TPCH Anlyzer");
		lblTpchAnlyzer.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblTpchAnlyzer.setBounds(10, 11, 142, 27);
		contentPane.add(lblTpchAnlyzer);
		
		JLabel lblTeam = new JLabel("Team");
		lblTeam.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTeam.setBounds(10, 60, 46, 14);
		contentPane.add(lblTeam);
		
		JTextPane txtpnGambirasioMatteo = new JTextPane();
		txtpnGambirasioMatteo.setEditable(false);
		txtpnGambirasioMatteo.setBackground(SystemColor.menu);
		txtpnGambirasioMatteo.setText("Gambirasio Matteo");
		txtpnGambirasioMatteo.setBounds(10, 74, 142, 77);
		contentPane.add(txtpnGambirasioMatteo);
		
		JLabel logo_unibg = new JLabel("");
		logo_unibg.setIcon(new ImageIcon(Credits.class.getResource("/images/logo_unibg.png")));
		logo_unibg.setBounds(194, 11, 80, 80);
		contentPane.add(logo_unibg);
	}
}
