import java.util.Scanner;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

class FileManager{
  
  public static final String tokenSeparator = " ";
  public static final String endOfLine = "0";
  public static final String lineSeparator = "\n";
  public static final String negation = "-";

  public static void writeToFile(String result, String fileName){
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
      writer.write(result);
      writer.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static int findIndex(String[] arr, String item){
    String temp = item;
    //Strip out the negation sign
    if(temp.substring(0,negation.length()).equals(negation)){
      temp = item.substring(negation.length());
    }
    
    for(int i = 0; i < arr.length; i++){
      if (arr[i].equals(temp)) return i;
    }
    
    return -1;
  }
  
  public static String convertToDIMACS(String clause, String[] varList, String[] mapList){
    StringTokenizer st = new StringTokenizer(clause,tokenSeparator);
    String token = "";
    String result = "";
    while(st.hasMoreTokens()){
      token = st.nextToken();
      int index = findIndex(varList,token);
      if (index >= 0){
        if (token.substring(0,negation.length()).equals(negation)){
           result = result + negation + mapList[index] + tokenSeparator;
        }else {
          result = result + mapList[index] + tokenSeparator;
        }  
      }
    }
    
    return result + endOfLine;
  }
  
  public static void convertToDIMACSFile(String fileIn, String fileOut, String fileMap){
    try (BufferedReader reader = new BufferedReader(new FileReader(fileIn))){
      String line = "";
      StringTokenizer st;
      
      String varInfo = reader.readLine();
      st = new StringTokenizer(varInfo,tokenSeparator);
      int dim = Integer.parseInt(st.nextToken());
      int maxColour = Integer.parseInt(st.nextToken());
      int maxColourDim = Integer.toString(maxColour-1,2).length();
      String varPrefix = st.nextToken();
      int numVar = ((int)Math.pow(2,dim))*maxColourDim;
      
      String[] varList = new String[numVar];
      for (int i = 0; i < numVar; i++){
        varList[i] = reader.readLine();
      }
      
      String[] mapList = new String[numVar];
      for (int i = 0; i < numVar; i++){
        mapList[i] = "" + (i + 1);
      }
      
      int numClauses = Integer.parseInt(reader.readLine());
      String clauses = "";
      String newClause = "";
      
      for (int i = 0; i < numClauses; i++){
        newClause = convertToDIMACS(reader.readLine(),varList,mapList);
        clauses = clauses + newClause + lineSeparator;
      }
      
      String outputHeader = "p cnf" + tokenSeparator + numVar + tokenSeparator + numClauses + lineSeparator;
      String output = outputHeader + clauses;
      writeToFile(output,fileOut);
      
      String mapping = "";
      for (int i = 0; i < numVar; i++){
        mapping = mapping + varList[i] + tokenSeparator + mapList[i] + lineSeparator;
      }
      String mappingHeader = "" + dim + tokenSeparator + maxColour + tokenSeparator + varPrefix + lineSeparator;
      writeToFile(mappingHeader + mapping,fileMap);
      
      reader.close();
    }catch (IOException e){
      e.printStackTrace();
    }
  }
  
  public static String getExHypercubeFormulaFromFile(String inputFile, String varPrefix, int dim, int maxDistance, int maxColour){
  	try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
  		reader.readLine();
  		int size = Integer.parseInt(reader.readLine());
  		String[] exclusiveList = new String[size];
  		for (int i=0;i<size;i++){
  			exclusiveList[i] = reader.readLine();
  		}
  		reader.close();
  		return HyperCubeFormula.getExHyperCubeFormula(varPrefix,dim,maxDistance,maxColour,exclusiveList);
  		
  	}catch (IOException e){
  	  e.printStackTrace();
  	}
  	
  	return "";
 }
 /*
  public static void extractFromModel(String modelFile, String mapFile, String outputFile){
  	try (BufferedReader modelReader = new BufferedReader(new FileReader(modelFile))){
  		try (BufferedReader mapReader = new BufferedReader(new FileReader(mapFile))){
  			//Read info from the mapping file and store them into varName[] and mapValue[]
  			String varInfo = mapReader.readLine();
  			StringTokenizer st = new StringTokenizer(varInfo,tokenSeparator);
  			int dim = Integer.parseInt(st.nextToken());
  			int maxColour = Integer.parseInt(st.nextToken());
  			String varPrefix = st.nextToken();
  			
  			int maxColourDim = Integer.toString(maxColour,2).length();
  			int numVar = ((int)Math.pow(2,dim))*maxColourDim;
  			
  			String[] varName = new String[numVar];
  			int[] mapValue = new int[numVar];
  			String line = "";
  			for (int i = 0; i < numVar; i++){
  				line = mapReader.readLine();
  				st = new StringTokenizer(line,tokenSeparator);
  				varName[i] = st.nextToken();
  				mapValue[i] = Integer.parseInt(st.nextToken());
  			}
  			
  			//Now we proceed to the model file
  			String model = modelReader.readLine();
  			st = new StringTokenizer(model,tokenSeparator);
  			int[] modelValue = new int[numVar];
  			String boolValue = "";
  			for (int i = 0; i < numVar; i++){
  				boolValue = st.nextToken();
  				int index;
  				if(boolValue.substring(0,negation.length()).equals(negation)){
  					index = Integer.parseInt(boolValue.substring(negation.length()));
  					modelValue[index-1] = 0;
  				}else {
  					index = Integer.parseInt(boolValue);
  					modelValue[index-1] = 1;
  				}	
  			}
  			
  			//The reverse engineering: construct the decimals from binaries
  			int numVertices = (int)Math.pow(2,dim);
  			int[] colourMap = new int[numVertices];
  			String binaryForm = "";
  			for (int i = 0; i < numVertices; i++){
  				binaryForm = "";
  				for(int j = 0; j < maxColourDim; j++){
  					binaryForm = binaryForm + modelValue[i*dim + j];
  				}
  				colourMap[i] = Integer.parseInt(binaryForm,2);
  			}
  			
  			mapReader.close();
  			
  		}catch (IOException mapError){
  			mapError.printStackTrace();
  		}
  		
  		modelReader.close();
  	}catch (IOException modelError){
  		modelError.printStackTrace();		
  	}	
  	
  }
  
  */
  
  public static void extractFromModel(String modelFile, String outputFile, int dim, int colourDim){
  	try (BufferedReader modelReader = new BufferedReader(new FileReader(modelFile))){
  	
  		//Now we proceed to the model file
  		String model = modelReader.readLine();
  		StringTokenizer st = new StringTokenizer(model,tokenSeparator);
  		int numVertices = (int)Math.pow(2,dim);
  		int numVar = numVertices*colourDim;
  		int[] modelValue = new int[numVar];
  		String boolValue = "";
  		for (int i = 0; i < numVar; i++){
  			boolValue = st.nextToken();
  			int index;
  			if(boolValue.substring(0,negation.length()).equals(negation)){
  				index = Integer.parseInt(boolValue.substring(negation.length()));
  				modelValue[index-1] = 0;
  			}else {
  				index = Integer.parseInt(boolValue);
  				modelValue[index-1] = 1;
  			}	
  		 }
  			
  		//The reverse engineering: construct the decimals from binaries
  		int[] colourMap = new int[numVertices];
  		String binaryForm = "";
  		for (int i = 0; i < numVertices; i++){
  			binaryForm = "";
  			for(int j = 0; j < colourDim; j++){
  				binaryForm = binaryForm + modelValue[i*colourDim + j];
  			}
  			colourMap[i] = Integer.parseInt(binaryForm,2);
  		}
  		
  		//Copy to file
  		String result = "";
  		for (int i = 0; i < numVertices; i++){
  			result = result + colourMap[i] + lineSeparator;
  		}
  		writeToFile(numVertices + lineSeparator + result,outputFile);
  		
  		modelReader.close();
  	}catch (IOException modelError){
  		modelError.printStackTrace();		
  	}		
  }
  
  public static String checkValidColouring(int[] arr, int dim, int maxDistance, int maxColour){
  	//Check the limit colour condition
  	for (int i = 0; i < arr.length; i++){
  		if (arr[i] >= maxColour) return "false" + lineSeparator + "E1" + tokenSeparator + i + tokenSeparator + arr[i];
  	}
  	
  	//Check the distance condition
  	int distance = 0;
  	for (int i = 0;i < arr.length; i++){
  		for (int j = i+1; j < arr.length; j++){
  			distance = HyperCubeFormula.getDistance(CNFConverter.toBinary(i,dim),CNFConverter.toBinary(j,dim)); 
  			if ((distance <= maxDistance) && (arr[i]==arr[j])){
  				return "false" + lineSeparator + "E2" + tokenSeparator + i + tokenSeparator + j + tokenSeparator + distance + tokenSeparator + arr[i];
  			}
  		}
  	}
  	
  	return "true";
  	
  }
  
  public static void checkValidColouringFromFile(String inputFile, String outputFile, int dim, int maxDistance, int maxColour){
  	try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
  		int numVar = Integer.parseInt(reader.readLine());
  		String errorMessage = "";
  		int[] colourArr = new int[numVar];
  		
  		for(int i = 0; i < numVar; i++){
  			colourArr[i] = Integer.parseInt(reader.readLine());
  		}
  		
  		reader.close();
  		writeToFile(checkValidColouring(colourArr,dim,maxDistance,maxColour),outputFile);
  	}catch (IOException e){
  		e.printStackTrace();
  	}
  }
  
  public static void main(String[] args){
  	
  	for (int i= 13;i<=14;i++){
  		writeToFile(HyperCubeFormula.getHyperCubeFormula("v",8,2,i),"8-2-" + i + ".prob");
  		convertToDIMACSFile("8-2-" + i + ".prob","8-2-" + i + ".cnf","8-2-" + i + ".map");
  	}
	
  }
}
