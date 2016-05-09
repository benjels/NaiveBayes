import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//EVERYTHING IN HERE BECAUSE IT IS SMALL AF
public class Main {

public static final String PATH_TO_TRAINING_INSTANCES = "data/spamLabelled.dat";

	public static void main(String[] args) throws IOException{
	//first, load all of the training instances into array of arrays
		boolean[][] trainingInstances = loadInstances(PATH_TO_TRAINING_INSTANCES);//new boolean[200][13];
	//now we have our instances, we can create all 12 of our little tables that we have for each of the instances.
		double[][] probabilityTables = generateProbabilityTables(trainingInstances);//new double[12][4];
	//now get the baseline kinda probabilities of spam vs not spam
	///...
	//now we are ready to do our classification on the testing set.
		//for each var in the testing instance, just look up the probability for that var being activated/not-activated for that class (we need to do both spam and not-spam separately) and then add that probability to the list of probabilities that we will multiply together (remember to add the probability of the class at the end too) Whichever class has the highest score wins.
	}

	private static double[][] generateProbabilityTables(boolean[][] trainingInstances) {
		// TODO Auto-generated method stub
		return null;
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


