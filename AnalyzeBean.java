package parsing;

public class AnalyzeBean {
	
	//步骤序号
	private int index;  
	//分析栈的字符串形式
    private String analyzeStackStr;  
    //剩余输入串
    private String remainingInputStr; 
    //推导所用产生式或匹配
    private String productionOrMatching;
    
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getAnalyzeStackStr() {
		return analyzeStackStr;
	}
	public void setAnalyzeStackStr(String analyzeStackStr) {
		this.analyzeStackStr = analyzeStackStr;
	}
	public String getRemainingInputStr() {
		return remainingInputStr;
	}
	public void setRemainingInputStr(String remainingInputStr) {
		this.remainingInputStr = remainingInputStr;
	}
	public String getProductionOrMatching() {
		return productionOrMatching;
	}
	public void setProductionOrMatching(String productionOrMatching) {
		this.productionOrMatching = productionOrMatching;
	}
	
}
