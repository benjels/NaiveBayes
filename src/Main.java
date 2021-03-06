import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//EVERYTHING IN HERE BECAUSE IT IS SMALL AF
public class Main {

public static final String PATH_TO_TRAINING_INSTANCES = "data/spamLabelled.dat";
public static final String PATH_TO_TESTING_INSTANCES = "data/spamLabelled.dat";

	public static void main(String[] args) throws IOException{
		//first, load all of the training instances into array of arrays
			boolean[][] trainingInstances = loadInstances(PATH_TO_TRAINING_INSTANCES);//new boolean[200][13];
		//now we have our instances, we can create all 12 of our little tables that we have for each of the instances.
			double[][] probabilityTables = generateProbabilityTables(trainingInstances);//new double[12][4];
		//now get the baseline kinda probabilities of spam vs not spam
		int nonSpamCount = 1;
		int spamCount = 1;
		for(boolean[] eachInstance: trainingInstances){
			if(eachInstance[12]){
				spamCount++;
			}else{
				nonSpamCount++;
			}
		}
		double nonSpamProb = (double)nonSpamCount/(double)(trainingInstances.length + 1);
		double spamProb = (double)spamCount/(double)(trainingInstances.length + 1);
		//now we are ready to do our classification on the testing set.
			//for each var in the testing instance, just look up the probability for that var being activated/not-activated for that class (we need to do both spam and not-spam separately) and then add that probability to the list of probabilities that we will multiply together (remember to add the probability of the class at the end too) Whichever class has the highest score wins.
		boolean[] dummyInstance = {true, true, false, true, false, true, true, false, false, true, true, false};
		System.out.println(classifyTestInstance(dummyInstance, probabilityTables, nonSpamProb, spamProb));

		//now we will do the testing haha
		test(PATH_TO_TESTING_INSTANCES, probabilityTables, nonSpamProb, spamProb);
	}

	private static void test(String pathToTestingInstances, double[][] probTables, double nonSpamProb, double spamProb) throws IOException {
		boolean[][] testingInstances = loadInstances(PATH_TO_TESTING_INSTANCES);
		int classifiedCorrectlyCount = 0;
		for(boolean[] eachInstance: testingInstances){
			if(classifyTestInstance(eachInstance, probTables, nonSpamProb, spamProb).equals("spam") && eachInstance[12] || classifyTestInstance(eachInstance, probTables, nonSpamProb, spamProb).equals("nonspam") && !eachInstance[12]){
				classifiedCorrectlyCount++;
				System.out.println("hell fuk ye");
			}else{
				System.out.println("lol nah");
			}
		}
		System.out.println("after testing, we classified a total of: " + classifiedCorrectlyCount + "/200 training instances correctly : )");

	}

	//returns the name of the class that the supplied instance seems to belong to
	private static String classifyTestInstance(boolean[] instance, double[][] probTables, double nonSpamProb, double spamProb) {

		//first find the probScore that it is not spam
		double runningTotal = 1;
		//System.out.println("\nNOW WORKING OUT THE PROB SCORE FOR NON SPAM: \n");
		for(int i = 0; i < 12; i++){
			if(instance[i]){
				runningTotal*=probTables[i][0];
				//System.out.println("asserted so multiplying by prob: " + probTables[i][0] + " at index" + i);
			}else{
				runningTotal*=probTables[i][1];
			//	System.out.println("not asserted so multiplying by prob: " + probTables[i][1] + " at index" + i);
			}
		}

		runningTotal *= nonSpamProb;
	//	System.out.println("so the probability that this instance is not spam is: " + runningTotal);
		double nonSpamScore = runningTotal;

		//now find probScore that it is spam haha
		runningTotal = 1;
		//System.out.println("\nNOW WORKING OUT THE PROB SCORE FOR SPAM: \n");
		for(int i = 0; i < 12; i++){
			if(instance[i]){
				runningTotal*=probTables[i][2];
		//		System.out.println("asserted so multiplying by prob: " + probTables[i][3] + " at index" + i);
			}else{
				runningTotal*=probTables[i][3];
			//	System.out.println("asserted so multiplying by prob: " + probTables[i][3] + " at index" + i);
			}
		}
		runningTotal *= spamProb;
	//	System.out.println("so the probability that this instance is spam is: " + runningTotal);
		double spamScore = runningTotal;

		if(spamScore >= nonSpamScore){
			return "spam";
		}else{
			return "nonspam";
		}
	}

	//key for the probability table that is returned indexes:
	//0: probability that the feature is activated when not spam
	//1: probability that the feature is not activated when not spam
	//2: probability that the feature is activated when spam
	//3: probability that the feature is not activated when spam
	//the ordering of the probability tables just follows the ordering of the 12 features in the spamLabelled.dat file.
	private static double[][] generateProbabilityTables(boolean[][] trainingInstances) {
		double[][] probabilityTable = new double[12][4];
		//first let's sort our training instances into two groups, spam and not spam
		ArrayList<boolean[]> spamInstances = new ArrayList<>();
		ArrayList<boolean[]> notSpamInstances = new ArrayList<>();
		for(boolean[] eachInstance: trainingInstances){
			if(eachInstance[12]){
				spamInstances.add(eachInstance);
			}else{
				notSpamInstances.add(eachInstance);
			}

		}
		//now, the probability that e.g. attribute 0 is activated given that the class is non-spam is given by forming a count of all of the instances that the attribute is true for in the non spam list and then dividing by the amount of instances in the non spam list


		for(int i = 0; i < 12; i++){
				int attributeTrueCount = 1;//NOTE THAT THESE ARE INITTED TO 1 TO AVOID ANY MULTIPLY BY 0 PROBLEMS
				int attributeFalseCount = 1;

			//generate the probabilities for the non spam instances instances
			for(boolean[] eachInstance: notSpamInstances){
				if(eachInstance[i]){
					attributeTrueCount++;
				}else{
					attributeFalseCount++;
				}
			}
			probabilityTable[i][0] = (double)attributeTrueCount/(double)(notSpamInstances.size() + 1);//iActivatedNonSpam
			probabilityTable[i][1] = (double)attributeFalseCount/(double)(notSpamInstances.size() + 1);//iNotActivatedNonSpam

			//generate the probabilities for the spam instances
			attributeTrueCount = 1;
			attributeFalseCount = 1;
			for(boolean[] eachInstance: spamInstances){
				if(eachInstance[i]){
					attributeTrueCount++;
				}else{
					attributeFalseCount++;
				}
			}
			probabilityTable[i][2] = (double)attributeTrueCount/(double)(spamInstances.size() + 1);//iActivatedSpam
			probabilityTable[i][3] = (double)attributeFalseCount/(double)(spamInstances.size() + 1);//iNotActivatedSpam



			System.out.println("the probability that " + i + " is activated given it is in the non spam class is: " + probabilityTable[i][0]);
			System.out.println("the probability that " + i + " is notactivated given it is in the non spam class is: " + probabilityTable[i][1]);
			System.out.println("the probability that " + i + " is activated given it is in the  spam class is: " + probabilityTable[i][2]);
			System.out.println("the probability that " + i + " is not activated given it is in the  spam class is: " + probabilityTable[i][3]);

		}


		return probabilityTable;
	}

	private static boolean[][] loadInstances(String path) throws IOException {
		ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("utf-8"));
		boolean[][] trainingInstances = new boolean[lines.size()][13];
		for(int i = 0; i < trainingInstances.length; i++){
			String[] eachLineString = lines.get(i).split(" ");
			//now we have each line as an array of Strings, convert to an array of bools
			boolean[] eachLineBoolean = new boolean[eachLineString.length];
			for(int j = 0; j < eachLineString.length; j++){
				if(eachLineString[j].equals("1")){
					eachLineBoolean[j] = true;
				}else{
					eachLineBoolean[j] = false;
				}

			}
			trainingInstances[i] = eachLineBoolean;
		}

/*		//test this shit
		for(boolean[] eachInstance: trainingInstances){
			System.out.println("\n\n\n");
			for(boolean eachVar: eachInstance){

				System.out.println(eachVar);
			}
		}*/
		return trainingInstances;
	}



	/*
	public static ArrayList<Iris> loadIrisDataFromFile(String path) throws IOException{
		//read the training data into a list of arrays of strings.
		ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(Paths.get(path), Charset.forName("utf-8"));
		ArrayList<Iris> irises = new ArrayList<>();
		for(String eachLine: lines){
			//there are some blank empty lines in the text files
			if(!eachLine.equals("")){
				//put the line into an array of strings
				String[] iris = eachLine.split("  ");//the values are separated by double spacing not tabs...
				//fill out the iris object and add it to the collection of irises
				irises.add(new Iris(Double.valueOf(iris[0]).doubleValue(), Double.valueOf(iris[1]).doubleValue(), Double.valueOf(iris[2]).doubleValue(), Double.valueOf(iris[3]).doubleValue(), iris[4]));
			}
		}
		return irises;
	}*/

}


