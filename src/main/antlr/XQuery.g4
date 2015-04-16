grammar XQuery;

@header {
package dk.martinbmadsen.xquery.parser;
}

// Absolute path
ap
  : 'doc(\'' TEXT '\')/'  rp
  | 'doc(\'' TEXT '\')//' rp;

// Relative path
rp
  : '*' | '.' | '..' | TEXT
  | '(' rp ')' | rp '/' rp | rp '//' rp | rp '[' f ']' | rp ',' rp
  ;

// Path filter
f
  : rp | rp '=' rp | rp ' eq ' rp | rp '==' rp | rp ' is ' rp
  | '(' f ')' | f ' and ' f  | f ' or ' f | 'not ' f

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


TEXT     : StringCharacters ;

fragment
StringCharacters :	StringCharacter+ ;

fragment
StringCharacter
  :	~["\\\'\[\]/ =] // match anything but " \ ' [ ] /
	 ;

//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines