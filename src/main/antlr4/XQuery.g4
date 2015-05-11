grammar XQuery;

@header {
package dk.martinbmadsen.xquery.parser;
}

// XQuery
xq
  : Var                                                       #xqVar
  | StringLiteral                                             #xqStringConstant
  | ap                                                        #xqAp
  | '(' xq ')'                                                #xqParenExpr
  | left=xq ',' right=xq                                      #xqConcat
  | xq '/' rp                                                 #xqSlash
  | '<' tagName1=Identifier '>' '{' xq '}' '</' tagName2=Identifier '>' #xqTagName
  | forClause letClause? whereClause? returnClause            #xqFLWR
  | letClause xq                                              #xqLet
  ;

// For Clause: for $var1 in $someList, $var2 in $var1)
forClause
  : 'for' Var 'in' xq (',' Var 'in' xq)*
  ;

// Let Clause: let $var1 := "superman", $var2 := "batman", ...
letClause
  : 'let' Var ':=' xq (',' Var ':=' xq)*
  ;

// Where Clause: where $var1 == $var2
whereClause
  : 'where' cond
  ;

// Return Clause: return $var1
returnClause
  : 'return' xq
  ;

// Condition
cond
  : left=xq ('='|'eq') right=xq                            #condValEqual
  | left=xq ('=='|'is') right=xq                           #condIdEqual
  | 'empty(' xq ')'                                        #condEmpty
  | 'some' Var 'in' xq (',' Var 'in' xq)* 'satisfies' cond #condSomeSatis
  | '(' cond ')'                                           #condParenExpr
  | left=cond 'and' right=cond                             #condAnd
  | left=cond 'or' right=cond                              #condOr
  | 'not' cond                                             #condNot
  ;


// Absolute path
ap
  : 'doc(' fileName=StringLiteral ')' slash=('/'|'//') rp
  ;

// Relative path
rp
  : '*'                               #rpWildcard
  | '.'                               #rpDot
  | '..'                              #rpDotDot
  | 'text()'                          #rpText
  | Identifier                        #rpTagName
  | '@' Identifier                    #rpAttr
  | '(' rp ')'                        #rpParenExpr
  | left=rp slash=('/'|'//') right=rp #rpSlash
  | rp '[' f ']'                      #rpFilter
  | left=rp ',' right=rp              #rpConcat
  ;

// Path filter
f
  : rp                                #fRp
  | left=rp ('eq'|'=')  right=rp      #fValEqual
  | left=rp ('is'|'==') right=rp      #fIdEqual
  | left=f  'and'       right=f       #fAnd
  | left=f  'or'        right=f       #fOr
  | '(' f ')'                         #fParen
  | 'not ' f                          #fNot
  ;

DOT      : '.' ;
UP       : '..';
WILDCARD : '*' ;
THEN     : ',' ;

// Separators
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
LANGLE : '<' | '</';
RANGLE : '>';

// Operators
ASSIGN : ':=';
EQLS   : '=';
EQUAL  : '==';
SLASH  : '/';
SSLASH : '//';
IS     : 'is';
EQ     : 'eq';
AND    : 'and';
OR     : 'or';
NOT    : 'not';

StringLiteral
  :   '\"' StringCharacters? '\"'
  ;

fragment
StringCharacters :	StringCharacter+ ;

fragment
SPACE : ' ' | '\u000C';

fragment
StringCharacter
  : ~[\"\\@] // technically, could match \, but might implement escape sequences (see antlr/grammars-v4/java/java.g4 @ GH)
	 ;

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

Var
  : '$' Identifier
  ;

Identifier
  : Letter LetterOrDigit*
  ;

fragment
Letter
  : [a-zA-Z_]
  ;

fragment
LetterOrDigit
  : [a-zA-Z0-9_-]
  ;