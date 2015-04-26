grammar XPath;

@header {
package dk.martinbmadsen.xquery.parser;
}

// Absolute path
ap
  : 'doc(' fileName=StringLiteral ')' slash=('/'|'//') rp
  ;

// Relative path
rp
  : '*'                               #rpWildcard
  | '..'                              #rpDotDot
  | '.'                               #rpDot
  | 'text()'                          #rpText
  | Identifier                        #rpTagName
  | '(' rp ')'                        #rpParenExpr
  | left=rp slash=('/'|'//') right=rp #rpSlash
  | rp '[' f ']'                      #rpFilter
  | left=rp ',' right=rp              #rpConcat
  | '@' attr=Identifier               #rpAttr
  ;

// Path filter
f
  : rp                                  #fRp
  | left=rp (' eq ' | ' = ')  right=rp  #fValEqual
  | left=rp (' is ' | ' == ')  right=rp #fIdEqual
  | left=f  ' and ' right=f             #fAnd
  | left=f  ' or '  right=f             #fOr
  | '(' f ')'                           #fParen
  | 'not ' f                            #fNot
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
EQLS   : ' = ';
EQUAL  : ' == ';
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