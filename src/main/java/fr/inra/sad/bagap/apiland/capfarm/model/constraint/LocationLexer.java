// Generated from Location.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.capfarm.model.constraint;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LocationLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__23=1, T__22=2, T__21=3, T__20=4, T__19=5, T__18=6, T__17=7, T__16=8, 
		T__15=9, T__14=10, T__13=11, T__12=12, T__11=13, T__10=14, T__9=15, T__8=16, 
		T__7=17, T__6=18, T__5=19, T__4=20, T__3=21, T__2=22, T__1=23, T__0=24, 
		INTEGER=25, ATTRIBUTE=26, WS=27;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'", "'\\u001B'"
	};
	public static final String[] ruleNames = {
		"T__23", "T__22", "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", 
		"T__15", "T__14", "T__13", "T__12", "T__11", "T__10", "T__9", "T__8", 
		"T__7", "T__6", "T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "INTEGER", 
		"ATTRIBUTE", "WS"
	};


	public LocationLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Location.g4"; }

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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\35\u0098\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b"+
		"\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\27\3"+
		"\27\3\30\3\30\3\31\3\31\3\32\6\32\u0085\n\32\r\32\16\32\u0086\3\33\5\33"+
		"\u008a\n\33\3\33\7\33\u008d\n\33\f\33\16\33\u0090\13\33\3\34\6\34\u0093"+
		"\n\34\r\34\16\34\u0094\3\34\3\34\2\2\35\3\3\5\4\7\5\t\6\13\7\r\b\17\t"+
		"\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27"+
		"-\30/\31\61\32\63\33\65\34\67\35\3\2\5\4\2C\\c|\5\2\62;C\\c|\5\2\13\f"+
		"\17\17\"\"\u009a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\39\3\2\2\2\5=\3\2\2\2\7A\3\2\2\2\tE\3\2\2\2\13G\3\2\2\2\rL\3\2\2\2\17"+
		"P\3\2\2\2\21S\3\2\2\2\23U\3\2\2\2\25W\3\2\2\2\27Y\3\2\2\2\31[\3\2\2\2"+
		"\33]\3\2\2\2\35`\3\2\2\2\37d\3\2\2\2!h\3\2\2\2#j\3\2\2\2%l\3\2\2\2\'n"+
		"\3\2\2\2)r\3\2\2\2+{\3\2\2\2-}\3\2\2\2/\177\3\2\2\2\61\u0081\3\2\2\2\63"+
		"\u0084\3\2\2\2\65\u0089\3\2\2\2\67\u0092\3\2\2\29:\7)\2\2:;\7H\2\2;<\7"+
		")\2\2<\4\3\2\2\2=>\7c\2\2>?\7n\2\2?@\7n\2\2@\6\3\2\2\2AB\7Z\2\2BC\7Q\2"+
		"\2CD\7T\2\2D\b\3\2\2\2EF\7V\2\2F\n\3\2\2\2GH\7C\2\2HI\7T\2\2IJ\7G\2\2"+
		"JK\7C\2\2K\f\3\2\2\2LM\7C\2\2MN\7P\2\2NO\7F\2\2O\16\3\2\2\2PQ\7@\2\2Q"+
		"R\7?\2\2R\20\3\2\2\2ST\7]\2\2T\22\3\2\2\2UV\7>\2\2V\24\3\2\2\2WX\7?\2"+
		"\2X\26\3\2\2\2YZ\7_\2\2Z\30\3\2\2\2[\\\7@\2\2\\\32\3\2\2\2]^\7>\2\2^_"+
		"\7?\2\2_\34\3\2\2\2`a\7C\2\2ab\7n\2\2bc\7n\2\2c\36\3\2\2\2de\7C\2\2ef"+
		"\7N\2\2fg\7N\2\2g \3\2\2\2hi\7H\2\2i\"\3\2\2\2jk\7*\2\2k$\3\2\2\2lm\7"+
		"+\2\2m&\3\2\2\2no\7)\2\2op\7V\2\2pq\7)\2\2q(\3\2\2\2rs\7F\2\2st\7K\2\2"+
		"tu\7U\2\2uv\7V\2\2vw\7C\2\2wx\7P\2\2xy\7E\2\2yz\7G\2\2z*\3\2\2\2{|\7-"+
		"\2\2|,\3\2\2\2}~\7.\2\2~.\3\2\2\2\177\u0080\7/\2\2\u0080\60\3\2\2\2\u0081"+
		"\u0082\7\60\2\2\u0082\62\3\2\2\2\u0083\u0085\4\62;\2\u0084\u0083\3\2\2"+
		"\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\64"+
		"\3\2\2\2\u0088\u008a\t\2\2\2\u0089\u0088\3\2\2\2\u008a\u008e\3\2\2\2\u008b"+
		"\u008d\t\3\2\2\u008c\u008b\3\2\2\2\u008d\u0090\3\2\2\2\u008e\u008c\3\2"+
		"\2\2\u008e\u008f\3\2\2\2\u008f\66\3\2\2\2\u0090\u008e\3\2\2\2\u0091\u0093"+
		"\t\4\2\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2\2\2\u0094"+
		"\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\b\34\2\2\u00978\3\2\2\2"+
		"\b\2\u0086\u0089\u008c\u008e\u0094\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}