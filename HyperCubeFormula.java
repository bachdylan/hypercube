class HyperCubeFormula{
  
  public static final String lineSeparator = "\n";
  public static final String varSeparator = "_";
  public static final String tokenSeparator = " ";
  
   public static int getDistance (String str1, String str2){
    
    int maxL = Math.min(str1.length(),str1.length());
    int counter = 0;
    for (int i = 0;i < maxL;i++){
      counter = counter + Math.abs(str1.charAt(i) - str2.charAt(i)); 
    }
    return counter;
  }
   
   public static String flattenArr(String[] arr){
     
     String result = "";
     for (int i = 0; i < arr.length; i++){
       result = result + arr[i] + lineSeparator;
     }
     
     return result;
   }
  
  public static String getHyperCubeFormula(String varPrefix, int dim, int maxDistance,int maxColour){
    String declareSection = "";
    String diffConstraintSection = "";
    String limitConstraintSection  = "";
    int numVar = (int)Math.pow(2,dim);
    int maxColourDim = Integer.toString(maxColour-1,2).length();
    
    int numClauses = 0;
    String[] tempArr;
    
    for (int i = 0; i < numVar; i++){
      for (int j = i+1; j < numVar; j++){
        if (getDistance(CNFConverter.toBinary(i,dim),CNFConverter.toBinary(j,dim)) <= maxDistance){
          tempArr = CNFConverter.getDiffFormula(varPrefix + CNFConverter.toBinary(i,dim),varPrefix + CNFConverter.toBinary(j,dim),maxColourDim);
          diffConstraintSection = diffConstraintSection + flattenArr(tempArr);
          numClauses = numClauses + tempArr.length;
        }
      }
    }
    
    for (int i = 0; i < numVar; i++){
      tempArr = CNFConverter.getLeqFormula(varPrefix + CNFConverter.toBinary(i,dim),maxColour - 1,maxColourDim);
      limitConstraintSection = limitConstraintSection + flattenArr(tempArr);
      numClauses = numClauses + tempArr.length;
    }

    declareSection = declareSection + dim + tokenSeparator + maxColour + tokenSeparator + varPrefix + lineSeparator;
    for (int i = 0;i < numVar; i++){
      for (int j = 0; j < maxColourDim; j++){
        declareSection = declareSection + varPrefix + CNFConverter.toBinary(i,dim) + varSeparator + j + lineSeparator;
      }
    }
    
    declareSection = declareSection + numClauses + lineSeparator;
    
    return declareSection + diffConstraintSection + limitConstraintSection;
  }
}

