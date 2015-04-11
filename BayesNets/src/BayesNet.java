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
	
	boolean sampleCorrectGivenEvidenceAtPos(ArrayList<Node> evidence, int sample, int pos){
		for(Node node : evidence){
			if(node.nodePos == pos){
				
				int evidenceState = (node.getState()) ? 1 : 0;
				if(sample != evidenceState){
					return false;
				}
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
		
outer:	 while(sampleCounter < samples){ //for(int i = 0; i < samples; i++){
			
			for(int j = 0; j < network.size(); j++){
				double prob = network.get(j).getConditionalProb();
				int toState = 0;
				
				if(rnd.nextDouble() < prob){
					toState = 1;
				}
				
				network.get(j).setState(toState);
			}
			int[] tempSample = new int[network.size()];
			for(int k = 0; k < network.size(); k++){
				tempSample[k] = (network.get(k).getState()) ? 1 : 0;
			}
			boolean accept = sampleCorrectGivenEvidence(evidence, tempSample);
			if(accept){
				for(int k = 0; k < network.size(); k++){
					sampleList[sampleCounter][k] = (network.get(k).getState()) ? 1 : 0 ;
				}
				sampleCounter++;
			}
		}
		
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
		
		System.out.println("Numer: " + numer);
		System.out.println("Denom: " + denom);
		System.out.println("Total: " + sampleCounter);
		
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
