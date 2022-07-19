// Generated from Combination.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.analysis.combination;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CombinationLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__23=1, T__22=2, T__21=3, T__20=4, T__19=5, T__18=6, T__17=7, T__16=8, 
		T__15=9, T__14=10, T__13=11, T__12=12, T__11=13, T__10=14, T__9=15, T__8=16, 
		T__7=17, T__6=18, T__5=19, T__4=20, T__3=21, T__2=22, T__1=23, T__0=24, 
		NUMBER=25, NAME=26, NODATA_Value=27, WS=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'", "'\\u001C'"
	};
	public static final String[] ruleNames = {
		"T__23", "T__22", "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", 
		"T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", "T__8", 
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "NUMBER", 
		"NAME", "NODATA_Value", "WS"
	};


	public CombinationLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Combination.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\36\u00a9\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3\3\3\3\4"+
		"\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3"+
		"\23\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3"+
		"\31\3\32\6\32\u0081\n\32\r\32\16\32\u0082\3\32\3\32\6\32\u0087\n\32\r"+
		"\32\16\32\u0088\5\32\u008b\n\32\3\33\5\33\u008e\n\33\3\33\7\33\u0091\n"+
		"\33\f\33\16\33\u0094\13\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\35\6\35\u00a4\n\35\r\35\16\35\u00a5\3\35\3\35\2"+
		"\2\36\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		"\3\2\5\4\2C\\c|\5\2\62;C\\c|\5\2\13\f\17\17\"\"\u00ad\2\3\3\2\2\2\2\5"+
		"\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2"+
		"\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33"+
		"\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2"+
		"\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2"+
		"\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5=\3\2\2\2"+
		"\7@\3\2\2\2\tC\3\2\2\2\13G\3\2\2\2\rI\3\2\2\2\17M\3\2\2\2\21P\3\2\2\2"+
		"\23R\3\2\2\2\25W\3\2\2\2\27Y\3\2\2\2\31[\3\2\2\2\33^\3\2\2\2\35a\3\2\2"+
		"\2\37d\3\2\2\2!f\3\2\2\2#j\3\2\2\2%o\3\2\2\2\'q\3\2\2\2)s\3\2\2\2+w\3"+
		"\2\2\2-y\3\2\2\2/{\3\2\2\2\61}\3\2\2\2\63\u0080\3\2\2\2\65\u008d\3\2\2"+
		"\2\67\u0095\3\2\2\29\u00a3\3\2\2\2;<\7\61\2\2<\4\3\2\2\2=>\7#\2\2>?\7"+
		"?\2\2?\6\3\2\2\2@A\7@\2\2AB\7?\2\2B\b\3\2\2\2CD\7C\2\2DE\7P\2\2EF\7F\2"+
		"\2F\n\3\2\2\2GH\7}\2\2H\f\3\2\2\2IJ\7n\2\2JK\7q\2\2KL\7i\2\2L\16\3\2\2"+
		"\2MN\7?\2\2NO\7?\2\2O\20\3\2\2\2PQ\7>\2\2Q\22\3\2\2\2RS\7g\2\2ST\7n\2"+
		"\2TU\7k\2\2UV\7h\2\2V\24\3\2\2\2WX\7\177\2\2X\26\3\2\2\2YZ\7@\2\2Z\30"+
		"\3\2\2\2[\\\7k\2\2\\]\7h\2\2]\32\3\2\2\2^_\7Q\2\2_`\7T\2\2`\34\3\2\2\2"+
		"ab\7>\2\2bc\7?\2\2c\36\3\2\2\2de\7#\2\2e \3\2\2\2fg\7r\2\2gh\7q\2\2hi"+
		"\7y\2\2i\"\3\2\2\2jk\7g\2\2kl\7n\2\2lm\7u\2\2mn\7g\2\2n$\3\2\2\2op\7*"+
		"\2\2p&\3\2\2\2qr\7+\2\2r(\3\2\2\2st\7g\2\2tu\7z\2\2uv\7r\2\2v*\3\2\2\2"+
		"wx\7,\2\2x,\3\2\2\2yz\7-\2\2z.\3\2\2\2{|\7.\2\2|\60\3\2\2\2}~\7/\2\2~"+
		"\62\3\2\2\2\177\u0081\4\62;\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u008a\3\2\2\2\u0084\u0086\7\60"+
		"\2\2\u0085\u0087\4\62;\2\u0086\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a\u0084\3\2"+
		"\2\2\u008a\u008b\3\2\2\2\u008b\64\3\2\2\2\u008c\u008e\t\2\2\2\u008d\u008c"+
		"\3\2\2\2\u008e\u0092\3\2\2\2\u008f\u0091\t\3\2\2\u0090\u008f\3\2\2\2\u0091"+
		"\u0094\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\66\3\2\2"+
		"\2\u0094\u0092\3\2\2\2\u0095\u0096\7P\2\2\u0096\u0097\7Q\2\2\u0097\u0098"+
		"\7F\2\2\u0098\u0099\7C\2\2\u0099\u009a\7V\2\2\u009a\u009b\7C\2\2\u009b"+
		"\u009c\7a\2\2\u009c\u009d\7X\2\2\u009d\u009e\7c\2\2\u009e\u009f\7n\2\2"+
		"\u009f\u00a0\7w\2\2\u00a0\u00a1\7g\2\2\u00a18\3\2\2\2\u00a2\u00a4\t\4"+
		"\2\2\u00a3\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a6\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\b\35\2\2\u00a8:\3\2\2\2"+
		"\n\2\u0082\u0088\u008a\u008d\u0090\u0092\u00a5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}