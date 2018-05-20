package parsing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import parsing.Parsing;

public class TestParsing {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根				
		File file = new File("/Users/lidawei/grammar1.txt");
		InputStream in = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
       
        try {
        	in = new BufferedInputStream(new FileInputStream(file));
			ir = new InputStreamReader(in,"utf-8");
			br = new BufferedReader(ir);
			String line = "";
			ArrayList<String> list = new ArrayList<String>();
			 //一行一行读取
            while((line = br.readLine()) != null){
                list.add(line);
            }
            Parsing parsing = new Parsing();
            parsing.setGrammarArray(list);
            parsing.initTerminAndNonTerminSet();
            parsing.getFirst();
            parsing.printFirst();
            parsing.setStartSymbol("E");
            parsing.getFollow();
            parsing.printFollow();
            parsing.getSelect();
            parsing.printSelect();
            parsing.genAnalyzeTable();
            parsing.printAnalyzeTable();
            AnalysisProcess analysisProcess = new AnalysisProcess();
            analysisProcess.setStartSymbol("E");
            analysisProcess.setParsing(parsing);
            analysisProcess.setRemainingInputStr("i+i*i#");
            analysisProcess.analyze();
            analysisProcess.printAnalysisProcess();
        }catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
                    br.close();
                }
                if(ir!=null){
                    ir.close();
                }
                if(in!=null){
                    in.close();
                }
			}catch (Exception e2) {
           }
		}
	}

}
