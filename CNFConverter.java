class CNFConverter{
  
  public static final String tokenSeparator = " ";
  public static final String negation = "-";
  public static final String varSeparator = "_";
  public static final String varPrefix = "v";
  
  public static String[] appendArr(String[] targetArr, String tail){
    String[] resultArr = new String[targetArr.length];
    
    for (int i = 0; i < resultArr.length; i++){
      resultArr[i] = targetArr[i] + tail;
    }
    
    return resultArr;
  }
  
  public static String[] combineArrs(String[] arr1, String[] arr2){
    int length = arr1.length + arr2.length;
    String[] resultArr = new String[length];
    
    for (int i = 0; i < arr1.length; i++){
      resultArr[i] = arr1[i];
    }
    
    for (int i = 0; i < arr2.length; i++){
      resultArr[arr1.length + i] = arr2[i];
    }
    
    return resultArr;
  }

  public static String[] getDiffFormula(String var1, String var2, int dim){
    
    String[] tempArrF1 = new String[dim];
    String[] tempArrF2 = new String[dim];

    String tempF1 = "";
    String tempF2 = "";
    for (int i = 0;i < dim; i++){
      tempF1 = var1 + varSeparator + i + tokenSeparator + var2 + varSeparator + i;
      tempF2 = negation + var1 + varSeparator + i + tokenSeparator + negation + var2 + varSeparator + i;
      tempArrF1[i] = tempF1;
      tempArrF2[i] = tempF2;
    }
    
    String[] tempArr = new String[1];
    tempArr[0] = "";
    String[] tempArrF3;
    String[] tempArrF4;
    
    
    for (int i = 0; i < dim; i++){
      tempArrF3 = appendArr(tempArr,tokenSeparator + tempArrF1[i]);
      tempArrF4 = appendArr(tempArr,tokenSeparator + tempArrF2[i]);
      tempArr = combineArrs(tempArrF3,tempArrF4);
    }
    
    return tempArr;
  }
  
    public static String toBinary (int num,int length){
    
    String temp = Integer.toString(num,2);
    int temp_length = temp.length();
    if (temp_length < length){
      for (int i = 1; i <= length - temp_length;i++){
        temp = "0" + temp;
      }
    }
    
    return temp;
  }
  
  public static String getDiffFormula(String var, int num, int dim){
    
    String binaryS = toBinary(num, dim);
    String result = "";
    for(int i = 0; i < dim; i++){
      if (binaryS.charAt(i) == '0'){
        result = result + tokenSeparator + var + varSeparator + i;
      }else {
        result = result + tokenSeparator + negation + var + varSeparator + i;
      }
    }
    
    return result;
  }
  
  //Special case for leq formula where the max colour = 12 and dim = 4
  public static String[] getLeq12Formula(String var){
  	
  	String[] result = new String[2];
  	result[0] = negation + var + varSeparator + 0 + tokenSeparator + negation + var + varSeparator + 1 + tokenSeparator + negation + var + varSeparator + 2;
  	result[1] = negation + var + varSeparator + 0 + tokenSeparator + negation + var + varSeparator + 1 + tokenSeparator + negation + var + varSeparator + 3;
  	return result;
  }
  
  //Special case for leq formula where the max colour = 11 and dim = 4
  public static String[] getLeq11Formula(String var){
  	
  	String[] result = new String[1];
  	result[0] = negation + var + varSeparator + 0 + tokenSeparator + negation + var + varSeparator + 1;
  	return result;
  }
  
  public static String[] getLeqFormula(String var, int num, int dim){
    
    if (num == 12 && dim == 4) return getLeq12Formula(var);
    if (num == 11 && dim == 4) return getLeq11Formula(var);
    
    int limit = (int)Math.pow(2,dim);
    String[] tempF = new String[1];
    String[] result = new String[0];
    
    for (int i = num + 1; i < limit; i++){
      tempF[0] = getDiffFormula(var,i,dim);
      result = combineArrs(result,tempF);
    }
    
    return result;
  }
}



