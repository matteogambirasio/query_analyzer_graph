package graph;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import model.EncSchemes;
import model.Operator;
import network.Network;
import parser.ParserNetwork;
import parser.ParserSimpleXML;
import parser.ParserXML;
import extra.TPCHUtils;
import extra.XmlFileFilter;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.UIManager;

public class Graph {

	private JFrame frmTpchAnalysis;
	private File queryInput;
	private File networkInput;
	private boolean queryLoaded;
	private boolean networkLoaded;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
						Graph window = new Graph();
						window.frmTpchAnalysis.setVisible(true);
						
						ParserXML parser = new ParserXML(); //parser che crea la struttura ad albero
						ParserSimpleXML parserSimple = new ParserSimpleXML(); //parser che non si preoccupa della struttura ma estrae gli operatori di una query
						ParserNetwork parsernetwork = new ParserNetwork();
											
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graph() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		/* look s.o. */
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
		
		queryInput = null;
		networkInput = null;
		
		frmTpchAnalysis = new JFrame();
		frmTpchAnalysis.setTitle("TPCH Analyzer");
		frmTpchAnalysis.setResizable(false);
		frmTpchAnalysis.setBounds(100, 100, 450, 680);
		frmTpchAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTpchAnalysis.getContentPane().setLayout(null);
		
		JLabel lblTpchQuery = new JLabel("TPCH Query");
		lblTpchQuery.setBounds(10, 11, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblTpchQuery);
		
		JLabel lblNetworkConfiguration = new JLabel("Network configuration");
		lblNetworkConfiguration.setBounds(10, 67, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblNetworkConfiguration);
		
		JLabel lblMinTime = new JLabel("Min time (sec.):");
		lblMinTime.setBounds(10, 123, 96, 14);
		frmTpchAnalysis.getContentPane().add(lblMinTime);
		
		JLabel lblNotCalculated = new JLabel("not calculated");
		lblNotCalculated.setBounds(116, 123, 318, 14);
		frmTpchAnalysis.getContentPane().add(lblNotCalculated);
		
		JLabel lblMinCost = new JLabel("Min cost (\u20AC):");
		lblMinCost.setBounds(10, 148, 96, 14);
		frmTpchAnalysis.getContentPane().add(lblMinCost);
		
		JLabel label_1 = new JLabel("not calculated");
		label_1.setBounds(116, 148, 318, 14);
		frmTpchAnalysis.getContentPane().add(label_1);
		
		JLabel lblOperations = new JLabel("Operations");
		lblOperations.setBounds(10, 213, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblOperations);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(191, 307, -120, -32);
		frmTpchAnalysis.getContentPane().add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(10, 238, 424, 359);
		frmTpchAnalysis.getContentPane().add(textPane);
		
		JLabel lblGeneratedOutput = new JLabel("Generated output:");
		lblGeneratedOutput.setBounds(10, 174, 96, 14);
		frmTpchAnalysis.getContentPane().add(lblGeneratedOutput);
		
		final JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setBounds(10, 608, 424, 23);
		frmTpchAnalysis.getContentPane().add(btnAnalyze);
		
		JButton btnLoadQuery = new JButton("Load");
		btnLoadQuery.setBounds(10, 33, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnLoadQuery);
		
		JButton btnLoadNetwork = new JButton("Load");
		btnLoadNetwork.setBounds(10, 89, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnLoadNetwork);
		
		final JLabel lblLoadQuery = new JLabel("no input");
		lblLoadQuery.setBounds(191, 36, 46, 14);
		frmTpchAnalysis.getContentPane().add(lblLoadQuery);
		
		JLabel lblLoadNetwork = new JLabel("no input");
		lblLoadNetwork.setBounds(191, 92, 46, 14);
		frmTpchAnalysis.getContentPane().add(lblLoadNetwork);
		
		
		btnLoadQuery.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new XmlFileFilter()); //filtro sui file solo di tipo sql
				Integer returnAction = fileChooser.showOpenDialog(frmTpchAnalysis);				
				if(returnAction == 0)
				{
					queryInput = fileChooser.getSelectedFile();
					
					//verifico l'estensione
					Boolean correctExt = false;					
					String fileName = queryInput.getName();
					int extPos = fileName.lastIndexOf(".");
					if(extPos != -1) {
					   String ext = fileName.substring(extPos,fileName.length());
					   if(ext.toLowerCase().compareTo(".xml") == 0) //comparazione estensione
						   correctExt = true;
					}
					
					
					if(correctExt)
					{
						
						lblLoadQuery.setText(fileName);
						queryLoaded = true;
						if(queryLoaded && networkLoaded)
							btnAnalyze.setEnabled(true); //abilito l'analisi
						
					}
					else
					{
						lblLoadQuery.setText("No input");
						queryLoaded = false;
						btnAnalyze.setEnabled(false);						
					}
					
				}				
			}
		});
		
	}
}
