grammar Location;

@header {
	package fr.inra.sad_paysage.capfarm.model.constraint;
}

evaluate :
	localisation EOF
; 
 
localisation : (partout | plusminus? terme)? (plusminus terme)* ;

terme : parcelles | boolatt | numatt | distance | andterme | xorterme;

plusminus : '+' | '-' ;

parcelles : '[' INTEGER (',' INTEGER)* ']' ;

boolatt : '[' ATTRIBUTE ('=' (('T'|'\'T\'') | ('F'|'\'F\'')))? ']' ;

numatt : '[' ATTRIBUTE ('=' | '<' | '>' | '<=' | '>=') INTEGER ('.' INTEGER)? ']' ;

distance : 'DISTANCE' '(' ATTRIBUTE ('=' | '<' | '>' | '<=' | '>=') INTEGER ('.' INTEGER)? ')' ;

partout : 'ALL' |'All' | 'all' ;

andterme : 'AND' '(' localisation ',' localisation (',' localisation)* ')' ;

xorterme : 'XOR' '(' localisation ',' localisation (',' localisation)* ')' ;

INTEGER : ('0'..'9')+ ;

ATTRIBUTE : (('a'..'z') | ('A'..'Z')) (('a'..'z') | ('A'..'Z') | ('0'..'9'))* ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

