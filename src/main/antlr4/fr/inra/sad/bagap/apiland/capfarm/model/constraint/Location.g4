grammar Location;

evaluate :
	localisation EOF
; 
 
localisation : (partout | plusminus? terme)? (plusminus terme)* ;

terme : parcelles | boolatt | stringatt | numatt | distance | area | andterme | orterme | xorterme;

plusminus : '+' | '-' ;

parcelles : '[' TEXT (',' TEXT)* ']' ;

boolatt : '[' TEXT ('=' (('T'|'\'T\'') | ('F'|'\'F\'')))? ']' ;

stringatt : '[' TEXT '=' TEXT ']' ;

numatt : '[' TEXT ('=' | '<' | '>' | '<=' | '>=') INTEGER ('.' INTEGER)? ']' ;

distance : 'DISTANCE' '(' TEXT ('=' | '<' | '>' | '<=' | '>=') INTEGER ('.' INTEGER)? ')' ;

area : 'AREA' ('=' | '<' | '>' | '<=' | '>=') INTEGER ('.' INTEGER)? ;

partout : 'ALL' |'All' | 'all' ;

andterme : 'AND' '(' localisation ',' localisation (',' localisation)* ')' ;

orterme : 'OR' '(' localisation ',' localisation (',' localisation)* ')' ;

xorterme : 'XOR' '(' localisation ',' localisation (',' localisation)* ')' ;

INTEGER : ('0'..'9')+ ;

TEXT : [a-zA-Z0-9_]+;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

