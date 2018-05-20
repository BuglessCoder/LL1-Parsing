package parsing;

import java.util.ArrayList;
import java.util.Stack;

public class AnalysisProcess {
	
	//开始符
    private String startSymbol;
    
    //分析栈
    private Stack<String> analyzeStack;  
    
    //剩余输入串
    private String remainingInputStr; 
    
    //推导所用产生式或匹配
    private String productionOrMatching;
    
    //分析Bean的数组（用一个Bean表示分析过程的一行）
    private ArrayList<AnalyzeBean> analyzeBeanList;
    
    //经过处理后的文法（求出了三个集合和分析表）
    private Parsing parsing;
    

	public void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

	public void setRemainingInputStr(String remainingInputStr) {
		this.remainingInputStr = remainingInputStr;
	}

	public void setParsing(Parsing parsing) {
		this.parsing = parsing;
	}

	//初始化分析过程
	public AnalysisProcess() {
		super();
		// TODO 自动生成的构造函数存根
		analyzeStack = new Stack<String>();
        // "#"进栈  
        analyzeStack.push("#");      
	}
	
	//开始分析
	public void analyze() {  
		analyzeBeanList = new ArrayList<AnalyzeBean>();
		// 开始符进栈  
        analyzeStack.push(startSymbol);
        int index = 0;
        //当分析栈非空的时候就一直循环分析
        while (!analyzeStack.empty()){
        	index++;
        	//若栈顶元素不等于剩余输入串的最左字符则去分析表中找产生式
        	if(!analyzeStack.peek().equals(remainingInputStr.charAt(0)+"")){
        		String nowProduction = parsing.findProduction(parsing.getSelectMap(), 
        				analyzeStack.peek(), remainingInputStr.charAt(0)+"");       		
        		AnalyzeBean analyzeBean = new AnalyzeBean();
        		analyzeBean.setIndex(index);
        		analyzeBean.setAnalyzeStackStr(analyzeStack.toString());
        		analyzeBean.setRemainingInputStr(remainingInputStr);
        		String nowProductionRight = "";
        		if(nowProduction == null){
        			analyzeBean.setProductionOrMatching("错误！");
        		}
        		else{
        			//取到当前所用的产生式右部
        			nowProductionRight = nowProduction.split("->")[1];
        			analyzeBean.setProductionOrMatching(nowProduction);       			
        		}
        		//把表示当前这一行分析的bean加到数组里边去
        		analyzeBeanList.add(analyzeBean);
        		//将当前的分析栈中的栈顶元素出栈  
                analyzeStack.pop();  
                //将当前用到的产生式右部反序入栈  
        		if(nowProduction != null && nowProductionRight.charAt(0) != 'ε'){
        			//System.out.println("okok");
    				for(int i=nowProductionRight.length()-1;i>=0;i--){
    					analyzeStack.push(nowProductionRight.charAt(i)+"");
    				}
    			}
        		continue;
        	}
        	
        	//如果分析栈栈顶元素和当前剩余输入串最左字符相等,则匹配
            if(analyzeStack.peek().equals(remainingInputStr.charAt(0)+"")){
            	AnalyzeBean analyzeBean = new AnalyzeBean();
            	analyzeBean.setIndex(index);
            	analyzeBean.setAnalyzeStackStr(analyzeStack.toString());
            	analyzeBean.setRemainingInputStr(remainingInputStr);
            	if(analyzeStack.peek().equals("#")){
            		analyzeBean.setProductionOrMatching("接受");
            	}
            	else{
            		analyzeBean.setProductionOrMatching("“"+remainingInputStr.charAt(0)+"”匹配");
            	}           	
            	//把表示当前这一行分析的bean加到数组里边去
        		analyzeBeanList.add(analyzeBean);
        		//将当前的分析栈中的栈顶元素出栈  
                analyzeStack.pop();  
                //将当前剩余输入串的最左符号去掉
                remainingInputStr = remainingInputStr.substring(1);
                continue; 
            }
            
        }  
	}
	
	public void printAnalysisProcess(){
		System.out.println("LL(1)分析过程：");
		System.out.println("步骤\t\t分析栈\t\t剩余输入串\t\t推导所用产生式或匹配");
		for(AnalyzeBean analyzeBean : analyzeBeanList){
			System.out.print(analyzeBean.getIndex()+"\t"+String.format("%15s", analyzeBean.getAnalyzeStackStr())
				+"\t\t"+analyzeBean.getRemainingInputStr()+"\t\t\t"+analyzeBean.getProductionOrMatching());
			System.out.println();
		}
		
	}
    
}
