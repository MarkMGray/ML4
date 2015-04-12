import java.util.ArrayList;
import java.util.Arrays;


public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		 TODO Auto-generated method stub
//		Node n1 = new Node("1", new ArrayList<Node>(), new ArrayList<Double>(Arrays.asList(0.2, 0.8)), 1);
//		Node n2 = new Node("2", new ArrayList<Node>(Arrays.asList(n1)), new ArrayList<Double>(Arrays.asList(0.5, 0.5, 0.2, 0.8)),2 );
//		Node n3 = new Node("3", new ArrayList<Node>(Arrays.asList(n1)), new ArrayList<Double>(Arrays.asList(0.9, 0.1, 0.3, 0.7)), 3 );
//		Node n4 = new Node("4", new ArrayList<Node>(Arrays.asList(n3,n2)), new ArrayList<Double>(Arrays.asList(0.1, 0.9, 0.8, 0.2, 0.9, 0.1, 0.7, 0.3)), 4 );
		
		Node n1 = new Node("1", new ArrayList<Node>(), new ArrayList<Double>(Arrays.asList(0.5, 0.5)), 1);
		Node n2 = new Node("2", new ArrayList<Node>(), new ArrayList<Double>(Arrays.asList(0.7, 0.3)), 2);
		Node n3 = new Node("3", new ArrayList<Node>(Arrays.asList(n1)), new ArrayList<Double>(Arrays.asList(0.6, 0.4, 0.7, 0.3)), 3);
		Node n4 = new Node("4", new ArrayList<Node>(Arrays.asList(n2)), new ArrayList<Double>(Arrays.asList(0.2, 0.8, 0.3, 0.7)), 4);
		Node n5 = new Node("5", new ArrayList<Node>(Arrays.asList(n2)), new ArrayList<Double>(Arrays.asList(0.9, 0.1, 0.1, 0.9)), 5);
		Node n6 = new Node("6", new ArrayList<Node>(Arrays.asList(n3)), new ArrayList<Double>(Arrays.asList(0.1, 0.9, 0.5, 0.5)), 6);
		Node n7 = new Node("7", new ArrayList<Node>(Arrays.asList(n3, n4, n5)), new ArrayList<Double>(Arrays.asList(0.5,0.5, 0.5, 0.5, 0.3, 0.7, 0.7, 
				0.3, 0.1, 0.9, 0.6, 0.4, 0.1, 0.9, 0.2, 0.8)), 7);
		Node n8 = new Node("8", new ArrayList<Node>(Arrays.asList(n3, n5)), new ArrayList<Double>(Arrays.asList(0.3, 0.7, 0.5, 0.5, 0.4, 0.6, 0.9, 0.1)), 8);
		Node n9 = new Node("9", new ArrayList<Node>(Arrays.asList(n6, n7)), new ArrayList<Double>(Arrays.asList(0.2,0.8, 0.6, 0.4, 0.3, 0.7, 0.9, 0.1)), 9);
		Node n10 = new Node("10", new ArrayList<Node>(Arrays.asList(n6, n8)), new ArrayList<Double>(Arrays.asList(0.9, 0.1, 0.9, 0.1, 0.9, 0.1, 0.5, 0.5)), 10);
		
		
		ArrayList<Node> bayesNet = new ArrayList<Node>(Arrays.asList(n1,n2,n3,n4,n5, n6, n7, n8, n9, n10));
		
		BayesNet bayes = new BayesNet(bayesNet);
		double averageRunningGibbs = 0;
		double averageRunningReject = 0;
		ArrayList<Double> gibbs = new ArrayList<Double>();
		ArrayList<Double> rej = new ArrayList<Double>();
		for(int z = 0; z < 1000; z++)
		{
			
			n3.setState(false);
			n4.setState(true);
			n5.setState(true);
			n7.setState(false);
			long startTime = System.nanoTime();
			double resGibs = bayes.queryNetworkGibs(new ArrayList<Node>(Arrays.asList(n7)), new ArrayList<Node>(Arrays.asList(n3,n4,n5)), 100, 1000);
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Gibs running time nano: " + duration);
			System.out.println("Gibs running time milli: " + duration / 1000000);
			averageRunningGibbs += duration/ 1000000;
			for(int i = 0; i < bayesNet.size(); i++)
			{
				bayesNet.get(i).setState(true);
			}
			startTime = System.nanoTime();
			
			n3.setState(false);
			n4.setState(true);
			n5.setState(true);
			n7.setState(false);
			double resRejection = bayes.queryNetworkRejection(new ArrayList<Node>(Arrays.asList(n7)), new ArrayList<Node>(Arrays.asList(n3,n4,n5)), 1000);
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			System.out.println("Rejection running time nano: " + duration);
			System.out.println("Rejection running time milli: " + duration / 1000000);
			System.out.println("Result Gibs: " + resGibs);
			System.out.println("Result Rejection: " + resRejection);
			gibbs.add(resGibs);
			rej.add(resRejection);
			averageRunningReject += duration/ 1000000;
		}
		
		System.out.println("Average Gibbs: " + averageRunningGibbs/10);
		System.out.println("Average Reject: " + averageRunningReject/10);
		
		System.out.print("gibs=[");
		for(int i = 0; i < gibbs.size(); i++){
			System.out.print(" " + gibbs.get(i));
			if(i != gibbs.size()-1)
				System.out.print(",");
		}
		System.out.print("];");
		System.out.println();
		System.out.print("rej=[");
		for(int i = 0; i < rej.size(); i++){
			System.out.print(" " + rej.get(i));
			if(i != rej.size()-1)
				System.out.print(",");
		}
		System.out.print("];");
		
		
		
	}

}
