// Define a grammar called Hello
grammar Hello;

@header {
package dk.martinbmadsen.xquery.parser;
}

r  : 'hello' ID ;         // match keyword hello followed by an identifier
ID : [a-z]+ ;             // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines