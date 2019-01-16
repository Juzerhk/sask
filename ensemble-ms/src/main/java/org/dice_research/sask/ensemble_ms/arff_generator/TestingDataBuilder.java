package org.dice_research.sask.ensemble_ms.arff_generator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestingDataBuilder {
	
	
	int x = 0;
	

	public static void main(String[] args) {
		
		
		TestingDataBuilder testingDataBuilder = new TestingDataBuilder();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Number of sentences that want to be tested:");
		int x = sc.nextInt();
		System.out.println("Number of Sentences are "+x);

		
		for(int i=1; i<= x; i++)
		{
		    Scanner scanner = new Scanner(System.in);
			System.out.println("Enter the training sentence:" + i);
			String testing_sentence = scanner.nextLine();
	         System.out.println("Entered sentence for testing  is " + testing_sentence);
			String trainingData = "'"+ testing_sentence + "'"+ "," + "?" ;
			testingDataBuilder.testingFileWriter(trainingData);
		
		}
		System.out.println("-------------------------------------------------------------");


	}

	private void testingFileWriter(String testing_sentence) {	   
	try 
	{
		
		FileWriter fw = new FileWriter("Dataset\\testingData.arff",true);
		if(x ==0)
		{
			fw.flush();
			fw.append("@relation testdata");
			fw.write(System.getProperty( "line.separator" ));
			fw.append("@attribute Sentences String");
			fw.write(System.getProperty( "line.separator" ));
		    fw.append("@attribute best_extractor {0,1,2}");
		    fw.write(System.getProperty( "line.separator" ));
			fw.append("@data" );
		    fw.write(System.getProperty( "line.separator" ));
		    x++;
		}
		
        fw.append(testing_sentence);
        fw.write(System.getProperty( "line.separator" ));
        fw.close();
	}
	catch (IOException e) {
		System.out.println("IO Execption");
		
	}

  
   }

}