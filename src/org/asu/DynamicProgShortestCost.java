package org.asu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author Sanjay Narayana
 *
 */	
public class DynamicProgShortestCost {

	private final static int INF = 99999;
	private final static int RAND_SEED = 50;

	public static void main(String[] args) throws IOException {

		int length = 0;
		int width = 0;
		int depth = 0;

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter non negative length\n");

		try {
			length = Integer.parseInt(reader.readLine());
		}
		catch (IOException e) {
			System.out.println("Invalid Length");
			System.exit(1);
		}


		System.out.println("Enter non negative width\n");

		try {
			width = Integer.parseInt(reader.readLine());
		}
		catch (IOException e) {
			System.out.println("Invalid Width");
			System.exit(1);
		}

		System.out.println("Enter non negative depth\n");
		try {
			depth = Integer.parseInt(reader.readLine());
		}
		catch (IOException e) {
			System.out.println("Invalid Depth");
			System.exit(1);
		}

		if(length < 0 || width < 0 || depth < 0 ) {
			System.out.println("negative value entered");
			System.exit(1);
		}

		int numberOfIterations = (length-1) + (width - 1) + (depth - 1); //Number of iterations the algorithm executes
		int numberOfPoints = length * width * depth; //The number of cubes in the L,W,D cuboid

		int widthDepthProduct = width*depth;
		int minCostOfPointsAfterEachIteration[] = new int[numberOfPoints];

		int costMatrix[][] = new int[numberOfPoints][numberOfPoints]; // Cost Matrix - only used for displaying the input cost matrix and not during computation.

		int successivePoints[] = new int[numberOfPoints]; //Store The next node along the path

		Map<String, Integer> costMap = new HashMap<>(); // sparse implementation of the cost matrix used while computing the shortest distances

		Arrays.fill(minCostOfPointsAfterEachIteration, INF);
		minCostOfPointsAfterEachIteration[numberOfPoints-1] = 0;
		successivePoints[numberOfPoints-1] = 0;
		Random rand = new Random();


		/*Use randomly generated numbers as the costs.
		 * Generate points in order of depth, width and length.
		 * Point 0 represents (0,0,0), point 1 - (0,0,1) and so on.
		 */
		for(int i=0;i<numberOfPoints;i++)
		{
			int iSumWidthDepthProduct = i+widthDepthProduct;
			int iDividedByWidtProduct = i/widthDepthProduct;
			for(int j=0;j<numberOfPoints;j++)
			{
				int jDividedByWidtProduct = j/widthDepthProduct;
				String key = ""+i+","+j;
				if(i == j)
				{
					costMatrix[i][j] = 0;
				}
				else if((j == iSumWidthDepthProduct) && (j < numberOfPoints))	//edge (a,b,c) to (a+1,b,c)
				{
					int cost = rand.nextInt(RAND_SEED) + 1;
					costMatrix[i][j] = cost;
					costMap.put(key,cost);
				}	
				else if((j == i+depth) && (jDividedByWidtProduct == iDividedByWidtProduct)) 	//edge (a,b,c) to (a,b+1,c)
				{
					int cost = rand.nextInt(RAND_SEED) + 1;
					costMatrix[i][j] = cost;
					costMap.put(key,cost);
				}
				else if((j == i+1) && ((jDividedByWidtProduct == iDividedByWidtProduct) && j%depth != 0)) 	//edge (a,b,c) to (a,b,c+1)
				{
					int cost = rand.nextInt(RAND_SEED) + 1;
					costMatrix[i][j] = cost;
					costMap.put(key,cost);
				}
				else
				{
					costMatrix[i][j] = INF;
				}
			}	
		}
		System.out.println("The cost Matrix is:");
		for (int i=0; i<numberOfPoints; ++i)
		{
			for (int j=0; j<numberOfPoints; ++j)
			{
				if (costMatrix[i][j]==INF)
					System.out.print("INF ");
				else
					System.out.print(costMatrix[i][j]+"   ");
			}
			System.out.println();
		}

		for(int i=1;i<=numberOfIterations;i++)
		{
			for(int j=numberOfPoints-1;j>=1;j--)  //foreach((a,b,c) cube such that it is at distance a+b+c+(i-1) from (L-1,W-1,D-1)) 
			{
				int nodeRemainderLength = j % widthDepthProduct;
				int nodeRemainderDepth = j % depth;
				int nodeLength = j/widthDepthProduct;
				int nodeWidth = (nodeRemainderLength)/depth;
				int nodeDepth = nodeRemainderDepth;
				if(nodeLength + nodeWidth + nodeDepth + (i-1) == numberOfIterations)
				{
					for(int k=numberOfPoints-2;k>=0;k--) //foreach((l,w,d) such that there is an edge between it and (a,b,c))
					{
						String key = ""+k+","+j;

						if((costMap.get(key) != null)  && 
								(minCostOfPointsAfterEachIteration[k] > minCostOfPointsAfterEachIteration[j] + costMap.get(key)))
						{
							minCostOfPointsAfterEachIteration[k] =  minCostOfPointsAfterEachIteration[j] + costMap.get(key);
							successivePoints[k] = j;
						}	
					}
				}
			}	
		}

		int node = 0;
		String nodeCoordinates = "";
		System.out.println("\nThe minimum total excavation cost is:"+minCostOfPointsAfterEachIteration[0]);
		System.out.println("\nThe sequence of cubes to be excavated are:");
		System.out.print("(0,0,0) --> ");
		while(node != numberOfPoints - 1)
		{
			node = successivePoints[node];
			int nodeRemainderLength = node % widthDepthProduct;
			int nodeRemainderDepth = node % depth;
			int nodeLength = node/widthDepthProduct;
			int nodeWidth = (nodeRemainderLength)/depth;
			int nodeDepth = nodeRemainderDepth;
			nodeCoordinates = "("+nodeLength+","+nodeWidth+","+nodeDepth+")";
			System.out.print(nodeCoordinates);
			System.out.print((node == numberOfPoints - 1) ? "" : " --> ");
		}

	}

}
