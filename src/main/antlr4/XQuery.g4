grammar XQuery;

@header {
package dk.martinbmadsen.xquery.parser;
}

// Absolute path
ap
  : 'doc(' fileName=StringLiteral ')' slash=('/'|'//') rp
  ;

// Relative path
rp
  : '*' #rpWildcard
  | '.' #rpDot
  | '..' #rpDotDot
  | 'text()' #rpText
  | Identifier #rpTagName
  | '@' Identifier #rpAttr
  | '(' rp ')' #rpParenExpr
  | left=rp slash=('/'|'//') right=rp #rpSlash
  | rp '[' f ']' #rpFilter
  | left=rp ',' right=rp #rpConcat
  ;

// Path filter
f
  : rp #fRp
  | left=rp equal=('='|'==') right=rp #fEqual
  | left=rp ' eq '  right=rp #fValEqual
  | left=rp ' is '  right=rp #fIdEqual
  | left=f  ' and ' right=f #fAnd
  | left=f  ' or '  right=f #fOr
  | '(' f ')' #fParen
  | 'not ' f #fNot
  ;

DOT :       '.';
UP : '..';
WILDCARD : '*';
THEN : ',';

// Separators
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';

// Operators
EQLS   : '=';
EQUAL  : '==';
SLASH  : '/';
SSLASH : '//';
IS     : ' is ';
EQ     : ' eq ';
AND    : ' and ';
OR     : ' or ';
NOT    : 'not ';

StringLiteral
  :   '\"' StringCharacters? '\"'
  ;

fragment
StringCharacters :	StringCharacter+ ;

fragment
StringCharacter
  : ~[\"\\@] // technically, could match \, but might implement escape sequences (see antlr/grammars-v4/java/java.g4 @ GH)
	 ;

//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

Identifier
  : Letter LetterOrDigit*
  ;

fragment
Letter
  : [a-zA-Z$_]
  ;

fragment
LetterOrDigit
  : [a-zA-Z0-9$_-]
  ;