grammar Combination;

evaluate : combination EOF ; 

combination : combinationwithcoma | condition | bloc ;

combinationwithcoma : '(' combination ')' ;

condition : 'if' '(' conditional ')' '{' combination '}' (('if' | 'elif') '(' conditional ')' '{' combination '}')* 'else' '{' combination '}' ;

conditional : genericboolterm ;

genericboolterm : (boolterm | booltermwithcoma | booltermnegation) | andgenericboolterm | orgenericboolterm ;

andgenericboolterm : 'AND' '(' genericboolterm ',' genericboolterm ( ',' genericboolterm )* ')';

orgenericboolterm : 'OR' '(' genericboolterm ',' genericboolterm ( ',' genericboolterm )* ')';

booltermwithcoma : '(' genericboolterm ')' ;

booltermnegation : '!' genericboolterm ;

boolterm : leftoperation boolop rightoperation ;

leftoperation : operation ;

rightoperation : operation ;

bloc : operation;

operation : term (mathop term)* ;

term : termwithcoma | termminus | NUMBER | name | function | NODATA_Value ;

termminus : '-' term ;

termwithcoma : '(' operation ')' ;

boolop : '>' | '>=' | '<' | '<=' | '==' | '!=' ;

name : NAME ;

mathop : '+' | '-' | '*' | '/' ;

function : function1param | function2params ;

function1param : ('log' | 'exp') '(' operation ')' ;

function2params : 'pow' '(' operation ',' NUMBER ')' ;

NUMBER : ('0'..'9')+ ('.' ('0'..'9')+)? ;

NAME : (('a'..'z') | ('A'..'Z')) (('a'..'z') | ('A'..'Z') | ('0'..'9'))* ;

NODATA_Value : 'NODATA_Value' ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

