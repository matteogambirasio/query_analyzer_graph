package network;

import java.util.ArrayList;

public class Network {
	
	private ArrayList<Node> nodes;
	
	public Network(ArrayList<Node> list)
	{
		nodes = list;
	}
	
	public String showNetwork()
	{
		String output = "Network: \n";
		for(int i = 0;i<nodes.size();i++)
			output += nodes.get(i).toString()+"\n";
		return output;
			
	}

	
	public Node getNodeByName(String name)
	{
		if(name.equals("NoNodeNeeded") || name == null)
			return getBestNode();
		
		for(int i = 0;i<nodes.size();i++)
			if(nodes.get(i).getName().equals(name))
				return nodes.get(i);
		return null;
	}

	public Node getBestNode() {
		
		//miglior nodo per throughput  -->pi� alto il throughput pi� le prestazioni della macchina sono alte..e quindi il bclo andr� meglio di
		//conseguenza
		Node output = nodes.get(0);
		for(int i = 1; i<nodes.size(); i++)
			if(nodes.get(i).getAesThroughput() > output.getAesThroughput())
				output = nodes.get(i);			
		
		return output;
	}
	
	public int getNodesNumber() //non coinvolgo i client nell'elaborazione
	{
		int output = 0;
		for(int i = 0; i<nodes.size(); i++)
			if(!nodes.get(i).getType().equals("Client"))
				output++;
		return output;
	}
	
	public Node getNodeByIndex(int i)
	{
		return nodes.get(i);
	}

	public int checkNodes() {
		return nodes.size();
	}

}
