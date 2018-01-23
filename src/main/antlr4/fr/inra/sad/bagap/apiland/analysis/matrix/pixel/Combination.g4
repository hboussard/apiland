grammar Combination;

evaluate : combination EOF ; 
 
combination : condition | operation ;

condition : 'if' '(' boolterm ')' '{' ifcombination '}' ('else' '{' elsecombination '}')? ;

ifcombination : combination ;

elsecombination : combination ;

logicalop : '&' | '&&' | 'AND' | '\\|' | '\\|\\|' | 'OR' ;

boolterm : leftoperation boolop rightoperation (logicalop boolterm)? ;

boolop : '>' | '>=' | '<' | '<=' | '==' | '!=' ;

operation : term (mathop term)* ;

leftoperation : operation ;

rightoperation : operation ;

term : termwithcoma | termminus | NUMBER | matrix | function | NODATA_Value ;

termminus : '-' term ;

termwithcoma : '(' term ')' ;

matrix : NAME ;

mathop : '+' | '-' | '*' | '/' ;

function : function1param | function2params ;

function1param : ('log' | 'exp') '(' operation ')' ;

function2params : 'pow' '(' operation ',' NUMBER ')' ;

NUMBER : ('0'..'9')+ ('.' ('0'..'9')+)? ;

NAME : (('a'..'z') | ('A'..'Z')) (('a'..'z') | ('A'..'Z') | ('0'..'9'))* ;

NODATA_Value : 'NODATA_Value' ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

