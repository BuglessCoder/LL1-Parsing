# LL1-Parsing
Java实现LL(1)语法分析过程

Example 1:
输入文法：	
E->TA
A->+TA
A->ε
T->FB
B->*FB
B->ε
F->i
F->(E)
		
开始符：E
待分析句子：i+i*i#
结果：
![image](https://github.com/BuglessCoder/LL1-Parsing/blob/master/Example%201.png）

Example 2:
输入文法：
S->aBc
S->bAB
A->aAb
A->b
B->b
B->ε

开始符：S
待分析句子：babb#
结果：
![image](https://github.com/BuglessCoder/LL1-Parsing/blob/master/Example%202.png）
