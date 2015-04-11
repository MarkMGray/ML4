import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class BayesNet {
	ArrayList<Node> network;
	Random rnd = new Random();
	
	BayesNet(ArrayList<Node> network){
		this.network = network;
	}
	
	Node getRandomNotInEvidence(ArrayList<Node> evidence){
		
		int randomIndex = rnd.nextInt(network.size());
		Node retNode = network.get(randomIndex);
		while(evidence.indexOf(retNode) != -1){
			randomIndex = rnd.nextInt(network.size());
			retNode = network.get(randomIndex);
		}
		
		return retNode;
	}
	
	boolean sampleCorrectGivenEvidence(ArrayList<Node> evidence, int[] sample){
		for(Node node : evidence){
			int pos = node.nodePos - 1;
			int evidenceState = (node.getState()) ? 1 : 0;
			if(sample[pos] != evidenceState){
				return false;
			}
		}
		return true;
	}
	
	double queryNetworkRejection(ArrayList<Node> sample, ArrayList<Node> evidence, int samples){
		
		int[][] sampleList = new int[samples][network.size()];
		int sampleCounter = 0;
		ArrayList<Boolean> startStates = new ArrayList<Boolean>();
		for(int i = 0; i < sample.size(); i++){
			startStates.add(sample.get(i).getState());
		}
		ArrayList<Boolean> evidenceStates = new ArrayList<Boolean>();
		for(int i = 0; i < evidence.size(); i++){
			evidenceStates.add(evidence.get(i).getState());
		}
		
		int rejected =0;
outer:	 while(sampleCounter < samples){ //for(int i = 0; i < samples; i++){
			int[] currSample = new int[network.size()];
			
			for(int k = 0; k < network.size(); k++){
				currSample[k] = (network.get(k).getState()) ? 1 : 0;
			}
			
			for(int j = 0; j < network.size(); j++){
				network.get(j).setState(true);
				double prob = network.get(j).getConditionalProb();
				int toState = 0;
				
				if(rnd.nextDouble() < prob){
					toState = 1;
				}
				network.get(j).setState(toState);
				boolean accept = true;
				for(int x = 0; x < evidence.size(); x++){
					
				}
				//boolean accept = sampleCorrectGivenEvidence(evidence, checkSample);
				
			}
			int[] checkSample = new int[network.size()];
			for(int k = 0; k < network.size(); k++){
				checkSample[k] = (network.get(k).getState()) ? 1 : 0;
			}
			for(int k = 0; k < evidence.size(); k++){
				int pos = evidence.get(k).nodePos - 1;
				network.get(pos).setState(currSample[pos]);
			}
			boolean accept = sampleCorrectGivenEvidence(evidence, checkSample);
			if(accept){
				System.out.print("Accept = [");
				for(int k = 0; k < network.size(); k++){
					System.out.print( " " + checkSample[k]);
				}
				System.out.print("]");
				System.out.println();
				for(int k = 0; k < network.size(); k++){
					sampleList[sampleCounter][k] = checkSample[k];
				}
				sampleCounter++;
			}else{
				rejected++;
			}
		}
		
		System.out.println("Rejected: "+ rejected);
		
		double denom = 0;
		double numer = 0;
		for(int i = 0; i < sample.size(); i++){
			sample.get(i).setState(startStates.get(i));
		}
		for(int i = 0; i < samples; i++){
			if(sampleCorrectGivenEvidence(evidence, sampleList[i])){
				denom++;
				if(sampleCorrectGivenEvidence(sample, sampleList[i])){
					numer++;
				}
			}
		}
		
		return numer / denom;
	}
	
	double queryNetworkGibs(ArrayList<Node> sample, ArrayList<Node> evidence, int itrs, int samples){
		
		Node randomNode = getRandomNotInEvidence(evidence);
		ArrayList<Boolean> startStates = new ArrayList<Boolean>();
		for(int i = 0; i < sample.size(); i++){
			startStates.add(sample.get(i).getState());
		}
		// Randomly initialize the network without changing evidence
		for(Node node: network){
			if(evidence.indexOf(node) == -1){
				node.setState(rnd.nextBoolean());
			}
		}
		int[][] sampleList = new int[samples][network.size()];
		for(int i = 0; i < samples; i++)
		{
			for(int j = 0; j < itrs; j++){
				
				double prob = randomNode.getConditionalProb();
				if(rnd.nextDouble() < prob)
				{
					randomNode.setState(true);
				}else{
					randomNode.setState(false);
				}
				
				randomNode = getRandomNotInEvidence(evidence);
			}
			for(int k = 0; k < network.size(); k++){
				sampleList[i][k] = (network.get(k).getState()) ? 1 : 0 ;
			}
		}
		
		double numer = 0;
		double denom = 0;
		//sample.setState(startState);
		for(int i = 0; i < sample.size(); i++){
			sample.get(i).setState(startStates.get(i));
		}
		for(int i = 0; i < samples; i++){
			if(sampleCorrectGivenEvidence(evidence, sampleList[i])){
				denom++;
				if(sampleCorrectGivenEvidence(sample, sampleList[i])){
					numer++;
				}
			}
		}
		return numer/ denom;
	}
	
}
