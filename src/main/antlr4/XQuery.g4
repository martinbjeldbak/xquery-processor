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
  | '(' rp ')' #rpParenExpr
  | left=rp '/' right=rp #rpSlash
  | left=rp '//' right=rp #rpSlashSlash
  | rp '[' f ']' #rpFilter
  | left=rp ',' right=rp #rpConcat
  ;

// Path filter
f
  : rp | leftrp=rp '=' rightrp=rp | leftrp=rp ' eq ' rightrp=rp | leftrp=rp '==' rightrp=rp | leftrp=rp ' is ' rightrp=rp
  | '(' f ')' | leftf=f ' and ' rightf=f  | leftf=f ' or ' rightf=f | 'not ' f
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
  : ~[\"\\] // technically, could match \, but might implement escape sequences (see antlr/grammars-v4/java/java.g4 @ GH)
	 ;

//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

Identifier
  : XPathLetter XPathLetterOrDigit*
  ;

fragment
XPathLetter
  : [a-zA-Z$_]
  ;

fragment
XPathLetterOrDigit
  : [a-zA-Z0-9$_]
  ;