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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;

import model.EncSchemes;
import network.Network;
import parser.ParserNetwork;
import parser.ParserXML;
import enviroment.Analyzer;
import enviroment.Attempt;
import extra.TPCHUtils;
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
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

public class Graph {

	private JFrame frmTpchAnalysis;
	private File queryInput;
	private File networkInput;
	private boolean queryLoaded;
	private boolean networkLoaded;
	private String outputDirectory;
	private boolean outputDirectoryLoaded;
	private String benchmarkDirectory;
	private boolean benchmark;
	public Credits creditsFrame; 
	
	public final JLabel lblReady = new JLabel("Ready");
	
	
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
	
	public void setStatus(String s)
	{
		if(s.equals("Running"))
		{
			lblReady.setText("Running");
			lblReady.setForeground(new Color(128,0,0));
		}
		else
		{
			lblReady.setText("Ready");
			lblReady.setForeground(new Color(0,128,0));
		}
		
	}

	/**
	 * Create the application.
	 */
	public Graph() {
		
		lblReady.setForeground(new Color(0, 128, 0));
		lblReady.setBounds(58, 301, 46, 14);
		
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
		outputDirectoryLoaded = false;
		benchmark = false;
		
		//path corrente del file
		outputDirectory = "not selected"; //directory di default
		benchmarkDirectory = "not selected";
		
		frmTpchAnalysis = new JFrame();
		frmTpchAnalysis.setIconImage(Toolkit.getDefaultToolkit().getImage(Graph.class.getResource("/images/logo.png")));
		frmTpchAnalysis.setTitle("TPCH Analyzer");
		frmTpchAnalysis.setResizable(false);
		frmTpchAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTpchAnalysis.getContentPane().setLayout(null);
		
		//dimensione dello schermo per centrare la finestra
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double width = screenSize.getWidth();
		Double height = screenSize.getHeight();		
		frmTpchAnalysis.setBounds((width.intValue()/2)-325, (height.intValue()/2)-340, 650, 680);		
		
		JLabel lblNetworkConfiguration = new JLabel("Network configuration");
		lblNetworkConfiguration.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNetworkConfiguration.setBounds(10, 85, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblNetworkConfiguration);
		
		final JLabel lblMinTime = new JLabel("Min time (sec.):");
		lblMinTime.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMinTime.setBounds(10, 218, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblMinTime);
		
		final JLabel lblMinTimeRes = new JLabel("not calculated");
		lblMinTimeRes.setBounds(191, 218, 368, 14);
		frmTpchAnalysis.getContentPane().add(lblMinTimeRes);
		
		final JLabel lblMinCost = new JLabel("Min cost (\u20AC):");
		lblMinCost.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMinCost.setBounds(10, 243, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblMinCost);
		
		final JLabel lblMinCostRes = new JLabel("not calculated");
		lblMinCostRes.setBounds(191, 243, 368, 14);
		frmTpchAnalysis.getContentPane().add(lblMinCostRes);
		
		JLabel lblOperations = new JLabel("Status:");
		lblOperations.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOperations.setBounds(10, 301, 54, 14);
		frmTpchAnalysis.getContentPane().add(lblOperations);
		
		JLabel lblGeneratedOutput = new JLabel("Generated output:");
		lblGeneratedOutput.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblGeneratedOutput.setBounds(10, 269, 136, 14);
		frmTpchAnalysis.getContentPane().add(lblGeneratedOutput);
		
		final JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.setBackground(new Color(173, 255, 47));
		btnAnalyze.setEnabled(false);
		btnAnalyze.setBounds(10, 608, 296, 23);
		frmTpchAnalysis.getContentPane().add(btnAnalyze);
		
		JButton btnLoadNetwork = new JButton("Load");
		btnLoadNetwork.setBounds(10, 107, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnLoadNetwork);
		
		final JLabel lblLoadNetwork = new JLabel("no input");
		lblLoadNetwork.setBounds(191, 110, 368, 14);
		frmTpchAnalysis.getContentPane().add(lblLoadNetwork);
		
		JButton btnOutputFolder = new JButton("Select");
		btnOutputFolder.setBounds(10, 159, 175, 23);
		frmTpchAnalysis.getContentPane().add(btnOutputFolder);
		
		final JLabel lblOutputFolder = new JLabel(outputDirectory);
		lblOutputFolder.setBounds(191, 163, 368, 14);
		frmTpchAnalysis.getContentPane().add(lblOutputFolder);
		
		JLabel lblOutputFolder_1 = new JLabel("Output folder");
		lblOutputFolder_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOutputFolder_1.setBounds(10, 139, 424, 14);
		frmTpchAnalysis.getContentPane().add(lblOutputFolder_1);
		
		final JLabel lblGeneratedOutputRes = new JLabel("");
		lblGeneratedOutputRes.setBounds(191, 265, 368, 14);
		frmTpchAnalysis.getContentPane().add(lblGeneratedOutputRes);
		
		final JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane jsp = new JScrollPane(textPane);
		jsp.setBounds(10, 326, 624, 271);
		frmTpchAnalysis.getContentPane().add(jsp);	
		
		JButton btnCredits = new JButton("Credits");
		btnCredits.setBounds(567, 297, 67, 23);
		frmTpchAnalysis.getContentPane().add(btnCredits);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "TPCH Benchmark", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(316, 11, 318, 54);
		frmTpchAnalysis.getContentPane().add(panel);
		
		JButton btnQueryFolder = new JButton("Query folder");
		panel.add(btnQueryFolder);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Single Query", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(10, 11, 296, 54);
		frmTpchAnalysis.getContentPane().add(panel_1);
		
		JButton btnLoadQuery = new JButton("Load");
		panel_1.add(btnLoadQuery);
		
		final JLabel lblLoadQuery = new JLabel("no input");
		panel_1.add(lblLoadQuery);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 76, 624, 2);
		frmTpchAnalysis.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 193, 624, 2);
		frmTpchAnalysis.getContentPane().add(separator_1);
		
		final JButton btnBenchmark = new JButton("Benchmark");
		btnBenchmark.setBounds(316, 608, 318, 23);
		frmTpchAnalysis.getContentPane().add(btnBenchmark);
		btnBenchmark.setEnabled(false);
		btnBenchmark.setBackground(new Color(173, 255, 47));
		
		frmTpchAnalysis.getContentPane().add(lblReady);
				
		btnBenchmark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ParserXML parser = new ParserXML(); //parser che crea la struttura ad albero
				ParserNetwork parsernetwork = new ParserNetwork();
				@SuppressWarnings("unused")
				TPCHUtils tpchUtils = new TPCHUtils();
								
							
				/* PARSING DEL NETWORK */
				Network network = new Network(parsernetwork.parseDocument(networkInput.getAbsolutePath()));
				if(network.checkNodes() <= 0)
				{
					//probabilmente il file non � corretto, o non ci sono nodi per qualche motivo
					textPane.setText("Error in network configuration file. No node found.");
					return;
				}
				
				
				/* CONFIGURAZIONE DEGLI OPERATORI */		
				EncSchemes encSchemes = new EncSchemes();
				
				textPane.setText("Please wait...");
				
				setStatus("Running");
				
				/* testo tutto il benchmark TPCH */
				for(int t = 1;t<=TPCHUtils.tpch_num;t++)
				{
					String resultFile = outputDirectory+"\\results_query_"+t+".txt";
					
					PrintWriter writer = null;
					try {
						writer = new PrintWriter(resultFile, "UTF-8");
					} catch (FileNotFoundException | UnsupportedEncodingException e) {
						e.printStackTrace();
					}	
					
					writer.println("QUERY "+t);
					parser.clearParser();
					//parser.parseDocument(getClass().getResource("/tpch/"+t+".xml").getPath()); //leggo direttamente dal package
					parser.parseDocument(benchmarkDirectory+"/"+t+".xml");	
					
					ArrayList<Attempt> results = new ArrayList<Attempt>();
					Analyzer analyzer = new Analyzer();
					
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
					Date date = new Date();
					writer.println("START ELABORATION: "+dateFormat.format(date));
					
					results = analyzer.Analyze(encSchemes, parser.operators, network);
					
					date = new Date();
					writer.println("END ELABORATION: "+dateFormat.format(date));
					
					writer.println("MIN TIME: "+analyzer.getMinTime()+ " sec.");
					writer.println("MIN COST: "+analyzer.getMinCost()+ " �");
					writer.println("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations());
					writer.println("MIN COST OPERATIONS: "+analyzer.getMinCostOperations());
					writer.println("RESULTS: "+results.toString());		
					writer.close();
					
					//mostro anche nella form
					lblMinCostRes.setText(String.valueOf(analyzer.getMinCost()));
					lblMinTimeRes.setText(String.valueOf(analyzer.getMinTime()));
					lblGeneratedOutputRes.setText(resultFile);
					
					String currentPane = textPane.getText();
					textPane.setText("TPCH Query "+t+": OK\n"+currentPane);
				}
				
				setStatus("Ready");
				
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
						if(queryLoaded && networkLoaded && outputDirectoryLoaded)
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
					outputDirectoryLoaded = true;
					File currentDirectory = fileChooser.getSelectedFile();
					outputDirectory = currentDirectory.getPath();
					lblOutputFolder.setText(outputDirectory);
					
					if(queryLoaded && networkLoaded && outputDirectoryLoaded)
						btnAnalyze.setEnabled(true); //abilito l'analisi
					if(networkLoaded && outputDirectoryLoaded && benchmark)
						btnBenchmark.setEnabled(true);
				}
			}
		});
		
		btnQueryFolder.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				Integer returnAction = fileChooser.showOpenDialog(frmTpchAnalysis);
								
				if(returnAction == 0)
				{
					//selezionata la directory
					benchmark = true;
					File currentDirectory = fileChooser.getSelectedFile();
					benchmarkDirectory = currentDirectory.getPath();
										
					if(queryLoaded && networkLoaded && outputDirectoryLoaded)
						btnAnalyze.setEnabled(true); //abilito l'analisi
					if(networkLoaded && outputDirectoryLoaded && benchmark)
						btnBenchmark.setEnabled(true);
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
						if(queryLoaded && networkLoaded && outputDirectoryLoaded)
							btnAnalyze.setEnabled(true); //abilito l'analisi
						if(networkLoaded && outputDirectoryLoaded && benchmark)
							btnBenchmark.setEnabled(true);
						
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
				@SuppressWarnings("unused")
				TPCHUtils tpchUtils = new TPCHUtils();
				
							
				/* PARSING DEL NETWORK */
				Network network = new Network(parsernetwork.parseDocument(networkInput.getAbsolutePath()));
				if(network.checkNodes() <= 0)
				{
					//probabilmente il file non � corretto, o non ci sono nodi per qualche motivo
					textPane.setText("Error in network configuration file. No node found.");
					return;
				}
				
				
				/* CONFIGURAZIONE DEGLI OPERATORI */		
				EncSchemes encSchemes = new EncSchemes();		
				
				/* ANALISI DEGLI OPERATORI */
				parser.parseDocument(queryInput.getAbsolutePath());	//costruzione della lista di operatori
				if(parser.operators.size() <= 0)
				{
					//probabilmente il file non � corretto, o non ci sono operatori per qualche motivo
					textPane.setText("Error in query configuration file. No operator found.");
					return;
				}
				
				textPane.setText("Please wait...");
				setStatus("Running");
				
				/* ANALISI DELLA QUERY */
				String resultFile = outputDirectory+"\\results_"+queryInput.getName()+".txt";
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(resultFile, "UTF-8");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}	
							
				ArrayList<Attempt> results = new ArrayList<Attempt>();
				Analyzer analyzer = new Analyzer();
				
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
				Date date = new Date();
				writer.println("START ELABORATION: "+dateFormat.format(date));
				
				results = analyzer.Analyze(encSchemes, parser.operators, network);
				
				date = new Date();
				writer.println("END ELABORATION: "+dateFormat.format(date));
				
				writer.println("MIN TIME: "+analyzer.getMinTime()+ " sec.");
				writer.println("MIN COST: "+analyzer.getMinCost()+ " �");
				writer.println("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations());
				writer.println("MIN COST OPERATIONS: "+analyzer.getMinCostOperations());
				writer.println("RESULTS: "+results.toString());		
				writer.close();
				
				//mostro anche nella form
				lblMinCostRes.setText(String.valueOf(analyzer.getMinCost()));
				lblMinTimeRes.setText(String.valueOf(analyzer.getMinTime()));
				lblGeneratedOutputRes.setText(resultFile);
				textPane.setText("MIN TIME OPERATIONS: "+analyzer.getMinTimeOperations()+"\nMIN COST OPERATIONS:"+analyzer.getMinCostOperations());
				
				setStatus("Ready");
				
			}
		});
		
		
		//credits
		btnCredits.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				creditsFrame = new Credits();
				creditsFrame.setVisible(true);
				
			}
		});
		
	}
}
