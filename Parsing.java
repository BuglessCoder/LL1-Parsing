package parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class Parsing {
	//分析表
    private String[][] analyzeTable;  
   
    //文法集
    private ArrayList<String> grammarArray;  
    
    //产生式集
    private HashMap<String, ArrayList<String>> productionMap;  
    
    //开始符
    private String startSymbol;  
    
    //非终结符集
    private TreeSet<String> nonTerminatorSet;  
    
    //终结符集
    private TreeSet<String> terminatorSet;  
    
    //FIRST集
    private HashMap<String, TreeSet<String>> firstMap; 
    
    //FOLLOW集
    private HashMap<String, TreeSet<String>> followMap; 
    
    //SELECT集
    private TreeMap<String, HashMap<String, TreeSet<String>>> selectMap;  
    
    public TreeMap<String, HashMap<String, TreeSet<String>>> getSelectMap() {
		return selectMap;
	}


	//初始化文法（程序入口，文法为产生式的集合）
	public Parsing() {
		grammarArray = new ArrayList<String>();
		nonTerminatorSet = new TreeSet<String>();
		terminatorSet = new TreeSet<String>();
		firstMap = new HashMap<String, TreeSet<String>>();  
		followMap = new HashMap<String, TreeSet<String>>();  
		selectMap = new TreeMap<String, HashMap<String, TreeSet<String>>>();  
	}

	//初始化文法（测试时用）
	public void setGrammarArray(ArrayList<String> grammarArray) {
		this.grammarArray = grammarArray;
	}
	
	//初始化开始符（测试时用）
	public void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}
	
	//初始化非终结符集合/终结符集合/表达式集合(求FIRST时用)
	public void initTerminAndNonTerminSet(){
		productionMap = new HashMap<String, ArrayList<String>>(); 
		for(String production : grammarArray){
			//取到每个产生式的左部和右部
			String left = production.split("->")[0];
			//左部一定是非终结符，所以直接加
			nonTerminatorSet.add(left);
		}
		for(String production : grammarArray){
			String right = production.split("->")[1];
			//右部一个一个字符取出，然后判断如果不是非终结符和"ε"就一定是终结符
			for(int i=0;i<right.length();i++){
				String singleSymble = right.charAt(i)+"";
				if(!nonTerminatorSet.contains(singleSymble) && !singleSymble.equals("ε")){
					terminatorSet.add(singleSymble);
				}
			}
		}
		for(String production : grammarArray){
			String left = production.split("->")[0];
			String right = production.split("->")[1];
			//将产生式按照左右部K-V放到Map里
			ArrayList<String> productionArray;		
			if(!productionMap.containsKey(left)){
				productionArray = new ArrayList<String>(); 		
			}
			else{
				productionArray = productionMap.get(left);
			}
			productionArray.add(right);
			productionMap.put(left, productionArray);
		}	
	}
	
	//获取First集
	public void getFirst() {
		for(String nonTermin : nonTerminatorSet){	
			ArrayList<String> rightArray = productionMap.get(nonTermin);
			for(String right : rightArray){		
				TreeSet<String> firstSet = firstMap.get(nonTermin);
				if(firstSet == null){
					firstSet = new TreeSet<String>();
				}				
				int flag = 0;
				for(int i=0;i<right.length();i++){
					String singleSymble = right.charAt(i)+"";
					if(nonTermin.equals("E")){
						for(String s : firstSet){
							System.out.println(s);
						}
					}
					flag = recursionFirst(firstSet, nonTermin, singleSymble);
					if(flag == 1){
						break;
					}
				}			
			}
		}
	}
	
	//递归求FIRST
	public int recursionFirst(TreeSet<String>firstSet, String nonTermin, String singleSymble){
		if(terminatorSet.contains(singleSymble) || singleSymble.equals("ε")){
			firstSet.add(singleSymble);
			firstMap.put(nonTermin, firstSet);
			return 1;
		}
		else if(nonTerminatorSet.contains(singleSymble)){
			ArrayList<String> arrayList = productionMap.get(singleSymble);
			for(String s : arrayList){
				String letter = s.charAt(0)+"";
				recursionFirst(firstSet, nonTermin, letter);
			}
		}
		return 1;
	}
	
	public void printFirst(){
    	System.out.println("First集：");
    	for(Map.Entry<String, TreeSet<String>> entry : firstMap.entrySet()){
    		System.out.println(entry.getKey() + ":  " + entry.getValue()); 
    	}
    	System.out.println();
    }
	
	//判断当前的非终结符的下一个是否是终结符(求FOLLOW时用)
	public boolean hasNextTermin(TreeSet<String> terminSet, String right, String nonTer) {  
        //在所有产生式的右部里寻找当前的非终结符
        if (right.contains(nonTer)) {    
            String next; 
            try {  
            	//取出找到的当前非终结符的下一个
            	next = right.substring(right.indexOf(nonTer)+1, right.indexOf(nonTer)+2); 
            } catch (Exception e) {  
                return false;  
            }  
            //如果当前非终结符的下一个是终结符
            if (terminSet.contains(next)) {  
                return true;  
            } else {  
                return false;  
            }  
        } else {  
            return false;  
        }  
    }
	
	//判断当前的非终结符的下一个是否是非终结符(求FOLLOW时用)
	public boolean hasNextNonTermin(TreeSet<String> nonTerminSet, String right, String nonTer) {    
		if (right.contains(nonTer)) {    
            String next; 
            try {  
            	//取出找到的当前非终结符的下一个
            	next = right.substring(right.indexOf(nonTer)+1, right.indexOf(nonTer)+2); 
            } catch (Exception e) {  
                return false;  
            }  
            //如果当前非终结符的下一个是非终结符
            if (nonTerminSet.contains(next)) {  
                return true;  
            } else {  
                return false;  
            }  
        } else {  
            return false;  
        }   
    }  
	
	//在已知当前非终结符的下一个是非终结符的情况下，判断其下一个非终结符是否能推空(求FOLLOW时用)
	public boolean nextNonTerminIsNull(TreeSet<String> nonTerminSet, String right, String nonTer, 
            HashMap<String, ArrayList<String>> productionMap) {    
        if (hasNextNonTermin(nonTerminatorSet, right, nonTer)) {  
            String next = getNext(right, nonTer);
            ArrayList<String> arrayList = productionMap.get(next);  
            if (arrayList.contains("ε")) {  
                return true;  
            }  
        }  
        return false;  
    } 
	
	//判断当前非终结符的右边是否为空（即当前非终结符是否在右部末尾）(求FOLLOW时用)
	public boolean hasNoNext(TreeSet<String> nonTerminSet, String right, String nonTer, 
            HashMap<String, ArrayList<String>> productionMap){
		String last = right.substring(right.length()-1);
		//如果该非终结符是右部的最后一个，则说明其右边为空
		if(nonTer.equals(last)){
			return true;
		}
		return false;
	}
	
	//获取一个产生式右部某非终结符的下一个符号(求FOLLOW时用)
	public String getNext(String right, String nonTer) {   
        if (right.contains(nonTer)) { 
            String next = "";  
            try {  
            	//获取一个产生式右部某非终结符右边的终结符
            	next = right.substring(right.indexOf(nonTer)+1, right.indexOf(nonTer)+2);  
            } catch (Exception e) {  
                return null;  
            }  
            return next;  
        }  
        return null;  
    } 
	
	//获取Follow集
	public void getFollow() {
		//初始化followMap，先为每一个非终结符映射一个空集合
		for(String nonTermin : nonTerminatorSet){
			TreeSet<String> treeSet = new TreeSet<String>();
			followMap.put(nonTermin, treeSet);
		}
		for(String nonTermin : nonTerminatorSet){
			//取出所有key，即非终结符,放到nonTerminSet里
			Set<String> nonTerminSet = productionMap.keySet();
			for(String nonTerm : nonTerminSet){
				//得到右部的集合
				ArrayList<String> rightArray = productionMap.get(nonTerm);
				for(String right : rightArray){
					TreeSet<String> followSet = followMap.get(nonTermin);
					recursionFollow(nonTermin, nonTermin, nonTerm, right, followSet);
				}
				
			}
		}
	}
	
	//递归求FOLLOW
	public void recursionFollow(String nowNonTermin, String nonTermin, String nonTerm, String right, 
			TreeSet<String> followSet){
		//(1)对于开始符来说，直接把#加进去
		if(nonTermin.equals(startSymbol)){
			followSet.add("#");
			followMap.put(nowNonTermin, followSet);
		}
		
		//在所有产生式右部寻找待求非终结符
		
		//(2)若待求非终结符的右边直接跟着终结符，则直接把该终结符加进去
		if(hasNextTermin(terminatorSet, right, nonTermin)){
			String next = getNext(right, nonTermin);
			followSet.add(next);
			followMap.put(nowNonTermin, followSet);
		}
		
		//(3)若待求非终结符的右边跟着非终结符，则把该非终结符的First集除掉"ε"后加进去
		if(hasNextNonTermin(nonTerminatorSet, right, nonTermin)){
			String next = getNext(right, nonTermin);
			TreeSet<String> firstSet = firstMap.get(next);
			followSet.addAll(firstSet);
			//如果First集里含有"ε"的话，"#"也要加进去
			if(firstSet.contains("ε")){
				followSet.add("#");
			}
			followSet.remove("ε");
			followMap.put(nowNonTermin, followSet);
			
			//(4)在(3)的前提下，若后边跟着的非终结符能推空，则需再把左部的Follow集加进去
			if(nextNonTerminIsNull(nonTerminatorSet, right, nonTermin, productionMap)){
				if(!nonTerm.equals(nonTermin)){
					Set<String> nonTerminSet = productionMap.keySet();
					for(String s : nonTerminSet){
						//得到右部的集合
						ArrayList<String> rightArray = productionMap.get(s);
						//循环递归
						for(String rightStr : rightArray){
							recursionFollow(nowNonTermin, nonTerm, s, rightStr, followSet);
						}
					}
				}
			}
			
		}
		
		//(5)若待求非终结符的右边为空，则把左部的Follow集加进去(操作同(4))
		if(hasNoNext(nonTerminatorSet, right, nonTermin, productionMap)){
			if(!nonTerm.equals(nonTermin)){
				Set<String> nonTerminSet = productionMap.keySet();
				for(String s : nonTerminSet){
					//得到右部的集合
					ArrayList<String> rightArray = productionMap.get(s);
					//循环递归
					for(String rightStr : rightArray){
						recursionFollow(nowNonTermin, nonTerm, s, rightStr, followSet);
					}
				}
			}			
		}		
	}
	
	public void printFollow(){
	    System.out.println("Follow集：");
	    for(Map.Entry<String, TreeSet<String>> entry : followMap.entrySet()){
	    	System.out.println(entry.getKey() + ":  " + entry.getValue()); 
	    }
	    System.out.println();
	}
	
	//判断产生式右部为空(求SELECT时用)
	public static boolean isEmpty(String right) {   
        if (right.equals("ε")) {  
            return true;  
        }  
        return false;  
    }
	
	//判断产生式右部是从终结符开始的(求SELECT时用)
	public static boolean isTerminStart(TreeSet<String> terminSet, String right) {  
        char charAt = right.charAt(0);  
        if (terminSet.contains(charAt+"")) {  
            return true;  
        }  
        return false;  
    }
	
	//判断产生式右部是从非终结符开始的(求SELECT时用)
	public static boolean isNonTerminStart(TreeSet<String> nonTerminSet, String right) {  
        char charAt = right.charAt(0);  
        if (nonTerminSet.contains(charAt+"")) {  
            return true;  
        }  
        return false;  
    } 
	
	//求SELECT集
	public void getSelect(){ 
		Set<String> nonTerminSet = productionMap.keySet(); 
		for(String leftNonTermin : nonTerminSet){
			ArrayList<String> rightArray = productionMap.get(leftNonTermin);		
			//最后selectMap的Value，也用一个Map存储
			HashMap<String, TreeSet<String>> selectMapValue = new HashMap<String, TreeSet<String>>();
			for(String right : rightArray){
				//每一个产生式求select集的结果集合
				TreeSet<String> selectSet = new TreeSet<String>();
				//(1)若产生式右部为空，则将左部的Follow集加进去
				if(isEmpty(right)){
					selectSet = followMap.get(leftNonTermin);					
				}
				//(2)若产生式右部是从终结符开始的，则直接将该终结符加进去
				else if(isTerminStart(terminatorSet, right)){
					selectSet.add(right.charAt(0)+"");
				}
				//(3)若产生式右部是从非终结符开始的，则将该非终结符的First集加进去
				else if(isNonTerminStart(nonTerminatorSet, right)){
					selectSet = firstMap.get(leftNonTermin);
				}
				selectSet.remove("ε");
				selectMapValue.put(leftNonTermin+"->"+right, selectSet);
				selectMap.put(leftNonTermin, selectMapValue);
			}
		}
	}
	
	public void printSelect(){
    	System.out.println("Select集：");
    	for(Map.Entry<String, HashMap<String, TreeSet<String>>> entry : selectMap.entrySet()){
    		System.out.println(entry.getKey() + ":  " + entry.getValue()); 
    	}
    	System.out.println();
    }
	
	//找到某行某列对应的产生式(获取LL(1)分析表时用)
	public String findProduction(TreeMap<String, HashMap<String, TreeSet<String>>> selectMap, 
			String nonTermin, String termin) {  
        try {  
        	//先找到当前非终结符的SELECT集（也是一个HashMap） 
            HashMap<String, TreeSet<String>> hashMap = selectMap.get(nonTermin); 
            //将找到的SELECT集的产生式取出
            Set<String> keySet = hashMap.keySet();  
            for (String production : keySet) {  
            	//找到该产生式对应的结果集
                TreeSet<String> treeSet = hashMap.get(production);
                //若结果集中包含当前的终结符，则返回该产生式
                if (treeSet.contains(termin)) {
                    return production;  
                }  
            }  
        } catch (Exception e) {  
            return null;  
        }  
        return null;  
    } 
	
	//获取分析表
	public void genAnalyzeTable(){
		//定义行与列，各用一个一维数组
		Object[] nonTerminArray = nonTerminatorSet.toArray();
		String[] terminArray = new String[terminatorSet.size()+1];
		Iterator<String> iterator = terminatorSet.iterator();
		int count = 0;
		while(iterator.hasNext()){
			terminArray[count] = iterator.next();
			count++;
		}
		//终结符不要忘了再一个"#"
		terminArray[terminatorSet.size()] = "#";
		//初始化分析表
		analyzeTable = new String[nonTerminArray.length+1][terminArray.length+1];
		//表格左上角的说明
		analyzeTable[0][0] = "Vn/Vt"; 
		
		//初始化首行，即放入所有终结符
		for(int i=0;i<terminArray.length;i++){
			analyzeTable[0][i+1] = terminArray[i]+"";
		}
		for(int i=0;i<nonTerminArray.length;i++){
			//初始化首列，即放入所有非终结符
			analyzeTable[i+1][0] = nonTerminArray[i]+"";
			for(int j=0;j<terminArray.length;j++){
				String nowProduction = findProduction(selectMap, nonTerminArray[i]+"", terminArray[j]+"");
				//System.out.println(nowProduction);
				if (nowProduction == null) {    
                    analyzeTable[i+1][j+1] = "×";  
                } 
				else{				
					analyzeTable[i+1][j+1] = nowProduction;
				}
			}
		}	
	}
	
	public void printAnalyzeTable(){
		System.out.println("LL(1)分析表：");
	    for(int i=0;i<analyzeTable.length;i++){
	    	for(int j=0;j<analyzeTable[i].length;j++){
	    		System.out.printf(analyzeTable[i][j]+"\t\t");
	    	}
	    	System.out.println();
	   	}
	    System.out.println();
	 }
	
	
}