import java.util.ArrayList;
import java.util.Arrays;


public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Node n1 = new Node("1", new ArrayList<Node>(), new ArrayList<Double>(Arrays.asList(0.2, 0.8)), 1);
		Node n2 = new Node("2", new ArrayList<Node>(Arrays.asList(n1)), new ArrayList<Double>(Arrays.asList(0.5, 0.5, 0.2, 0.8)),2 );
		Node n3 = new Node("3", new ArrayList<Node>(Arrays.asList(n1)), new ArrayList<Double>(Arrays.asList(0.9, 0.1, 0.3, 0.7)), 3 );
		Node n4 = new Node("4", new ArrayList<Node>(Arrays.asList(n3,n2)), new ArrayList<Double>(Arrays.asList(0.1, 0.9, 0.8, 0.2, 0.9, 0.1, 0.7, 0.3)), 4 );
		
		ArrayList<Node> bayesNet = new ArrayList<Node>(Arrays.asList(n1,n2,n3,n4));
		
		BayesNet bayes = new BayesNet(bayesNet);
		
		
		n3.setState(true);
		double res = bayes.queryNetwork(n2, new ArrayList<Node>(Arrays.asList(n3)), 100, 100);
		
		System.out.println("Result: " + res);
		
		
		
		
	}

}
