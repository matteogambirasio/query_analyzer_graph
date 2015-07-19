package graph;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.swing.JFrame;

import model.EncSchemes;
import network.Network;
import parser.ParserNetwork;
import parser.ParserXML;
import enviroment.Analyzer;
import enviroment.Attempt;
import extra.XmlFileFilter;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Graph {

	private JFrame frmTpchAnalysis;
	private File queryInput;
	private File networkInput;
	private boolean queryLoaded;
	private boolean networkLoaded;
	private String outputDirectory;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
						Graph window = new Graph();
						window.frmTpchAnalysis.setVisible(true);
					
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
		
		queryLoaded = false;
		networkLoaded = false;
		
		//path corrente del file
		String decodedPath = "";
		String path = Graph.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			decodedPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}		
		outputDirectory = decodedPath; //directory di default
		
		frmTpchAnalysis = new JFrame();
		frmTpchAnalysis.setTitle("TPCH Analyzer");
		frmTpchAnalysis.setResizable(false);
		frmTpchAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTpchAnalysis.getContentPane().setLayout(null);
		
		//dimensione dello schermo per centrare la finestra
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();		
		frmTpchAnalysis.setBounds((width.intValue()/2)-325, (height.intValue()/2)-340, 650, 680);		
		
		JLabel lblTpchQuery = new JLabel("TPCH Query");
		lblTpchQuery.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTpchQuery.setBounds(10, 11, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblTpchQuery);
		
		JLabel lblNetworkConfiguration = new JLabel("Network configuration");
		lblNetworkConfiguration.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNetworkConfiguration.setBounds(10, 67, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblNetworkConfiguration);
		
		final JLabel lblMinTime = new JLabel("Min time (sec.):");
		lblMinTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMinTime.setBounds(10, 193, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblMinTime);
		
		final JLabel lblMinTimeRes = new JLabel("not calculated");
		lblMinTimeRes.setBounds(156, 193, 448, 14);
		frmTpchAnalysis.getContentPane().add(lblMinTimeRes);
		
		final JLabel lblMinCost = new JLabel("Min cost (\u20AC):");
		lblMinCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMinCost.setBounds(10, 218, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblMinCost);
		
		final JLabel lblMinCostRes = new JLabel("not calculated");
		lblMinCostRes.setBounds(156, 218, 448, 14);
		frmTpchAnalysis.getContentPane().add(lblMinCostRes);
		
		JLabel lblOperations = new JLabel("Operations");
		lblOperations.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOperations.setBounds(10, 282, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblOperations);
		
		JLabel lblGeneratedOutput = new JLabel("Generated output:");
		lblGeneratedOutput.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGeneratedOutput.setBounds(10, 244, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblGeneratedOutput);
		
		final JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setEnabled(false);
		btnAnalyze.setBounds(10, 608, 624, 23);
		frmTpchAnalysis.getContentPane().add(btnAnalyze);
		
		JButton btnLoadQuery = new JButton("Load");
		btnLoadQuery.setBounds(10, 33, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnLoadQuery);
		
		JButton btnLoadNetwork = new JButton("Load");
		btnLoadNetwork.setBounds(10, 89, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnLoadNetwork);
		
		final JLabel lblLoadQuery = new JLabel("no input");
		lblLoadQuery.setBounds(191, 36, 243, 14);
		frmTpchAnalysis.getContentPane().add(lblLoadQuery);
		
		final JLabel lblLoadNetwork = new JLabel("no input");
		lblLoadNetwork.setBounds(191, 92, 243, 14);
		frmTpchAnalysis.getContentPane().add(lblLoadNetwork);
		
		JButton btnOutputFolder = new JButton("Select");
		btnOutputFolder.setBounds(10, 141, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnOutputFolder);
		
		final JLabel lblOutputFolder = new JLabel(outputDirectory);
		lblOutputFolder.setBounds(191, 145, 243, 14);
		frmTpchAnalysis.getContentPane().add(lblOutputFolder);
		
		JLabel lblOutputFolder_1 = new JLabel("Output folder");
		lblOutputFolder_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOutputFolder_1.setBounds(10, 121, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblOutputFolder_1);
		
		final JLabel lblGeneratedOutputRes = new JLabel("");
		lblGeneratedOutputRes.setBounds(156, 244, 448, 14);
		frmTpchAnalysis.getContentPane().add(lblGeneratedOutputRes);
		
		final JTextPane textPane = new JTextPane();
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setBounds(10, 296, 624, 301);
		frmTpchAnalysis.getContentPane().add(jsp);		
		
		
		//directory dell'output
		//SELEZIONE CARTELLA DI DESTINAZIONE 
		btnOutputFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				Integer returnAction = fileChooser.showOpenDialog(frmTpchAnalysis);
								
				if(returnAction == 0)
				{
					//selezionata la directory
					File currentDirectory = fileChooser.getSelectedFile();
					outputDirectory = currentDirectory.getPath();
					lblOutputFolder.setText(outputDirectory);
				}
			}
		});
		
		//caricamento della query
		btnLoadQuery.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new XmlFileFilter()); //filtro sui file solo di tipo xml
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
		
		//caricamento della configurazione di rete
		btnLoadNetwork.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new XmlFileFilter()); //filtro sui file solo di tipo xml
				Integer returnAction = fileChooser.showOpenDialog(frmTpchAnalysis);				
				if(returnAction == 0)
				{
					networkInput = fileChooser.getSelectedFile();
					
					//verifico l'estensione
					Boolean correctExt = false;					
					String fileName = networkInput.getName();
					int extPos = fileName.lastIndexOf(".");
					if(extPos != -1) {
					   String ext = fileName.substring(extPos,fileName.length());
					   if(ext.toLowerCase().compareTo(".xml") == 0) //comparazione estensione
						   correctExt = true;
					}
					
					
					if(correctExt)
					{
						
						lblLoadNetwork.setText(fileName);
						networkLoaded = true;
						if(queryLoaded && networkLoaded)
							btnAnalyze.setEnabled(true); //abilito l'analisi
						
					}
					else
					{
						lblLoadNetwork.setText("No input");
						networkLoaded = false;
						btnAnalyze.setEnabled(false);						
					}
					
				}				
			}
		});
		
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ParserXML parser = new ParserXML(); //parser che crea la struttura ad albero
				ParserNetwork parsernetwork = new ParserNetwork();
							
				/* PARSING DEL NETWORK */
				Network network = new Network(parsernetwork.parseDocument(networkInput.getAbsolutePath()));
				
				/* CONFIGURAZIONE DEGLI OPERATORI */		
				EncSchemes encSchemes = new EncSchemes();				
				
				/* ANALISI DELLE QUERY */
				String resultFile = outputDirectory+"\\results.txt";
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(resultFile, "UTF-8");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
							
				
				/* SINGOLA QUERY */
				writer.println("QUERY ");
				parser.parseDocument(queryInput.getAbsolutePath());	//costruzione della lista di operatori
				
				ArrayList<Attempt> results = new ArrayList<Attempt>();
				Analyzer analyzer = new Analyzer();
				results = analyzer.Analyze(encSchemes, parser.operators, network); //nalaisi
				
				
				writer.println("MIN TIME: "+analyzer.getMinTime()+ " sec.");
				writer.println("MIN COST: "+analyzer.getMinCost()+ " €");
				writer.println("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations());
				writer.println("MIN COST OPERATIONS: "+analyzer.getMinCostOperations());
				writer.println("RESULTS: "+results.toString());
				
				writer.close();
				
				//mostro anche nella form
				lblMinCostRes.setText(String.valueOf(analyzer.getMinCost()));
				lblMinTimeRes.setText(String.valueOf(analyzer.getMinTime()));
				lblGeneratedOutputRes.setText(resultFile);
				textPane.setText("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations()+"\nMIN COST OPERATIONS:"+analyzer.getMinCostOperations());
				
			}
		});
		
	}
}
