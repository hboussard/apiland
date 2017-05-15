// Generated from Location.g4 by ANTLR 4.4
package fr.inra.sad_paysage.apiland.capfarm.model.constraint;
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
		T__22=1, T__21=2, T__20=3, T__19=4, T__18=5, T__17=6, T__16=7, T__15=8, 
		T__14=9, T__13=10, T__12=11, T__11=12, T__10=13, T__9=14, T__8=15, T__7=16, 
		T__6=17, T__5=18, T__4=19, T__3=20, T__2=21, T__1=22, T__0=23, INTEGER=24, 
		ATTRIBUTE=25, WS=26;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'", "'\\u0015'", "'\\u0016'", "'\\u0017'", "'\\u0018'", 
		"'\\u0019'", "'\\u001A'"
	};
	public static final String[] ruleNames = {
		"T__22", "T__21", "T__20", "T__19", "T__18", "T__17", "T__16", "T__15", 
		"T__14", "T__13", "T__12", "T__11", "T__10", "T__9", "T__8", "T__7", "T__6", 
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "INTEGER", "ATTRIBUTE", 
		"WS"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\34\u0091\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20"+
		"\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\6\31"+
		"~\n\31\r\31\16\31\177\3\32\5\32\u0083\n\32\3\32\7\32\u0086\n\32\f\32\16"+
		"\32\u0089\13\32\3\33\6\33\u008c\n\33\r\33\16\33\u008d\3\33\3\33\2\2\34"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\3\2\5\4\2C\\"+
		"c|\5\2\62;C\\c|\5\2\13\f\17\17\"\"\u0093\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3"+
		"\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2"+
		"\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35"+
		"\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\3\67\3\2\2\2\5;\3\2\2\2\7?\3\2\2\2\tC\3\2\2\2\13E\3\2\2\2"+
		"\rI\3\2\2\2\17L\3\2\2\2\21N\3\2\2\2\23P\3\2\2\2\25R\3\2\2\2\27T\3\2\2"+
		"\2\31V\3\2\2\2\33Y\3\2\2\2\35]\3\2\2\2\37a\3\2\2\2!c\3\2\2\2#e\3\2\2\2"+
		"%g\3\2\2\2\'k\3\2\2\2)t\3\2\2\2+v\3\2\2\2-x\3\2\2\2/z\3\2\2\2\61}\3\2"+
		"\2\2\63\u0082\3\2\2\2\65\u008b\3\2\2\2\678\7)\2\289\7H\2\29:\7)\2\2:\4"+
		"\3\2\2\2;<\7c\2\2<=\7n\2\2=>\7n\2\2>\6\3\2\2\2?@\7Z\2\2@A\7Q\2\2AB\7T"+
		"\2\2B\b\3\2\2\2CD\7V\2\2D\n\3\2\2\2EF\7C\2\2FG\7P\2\2GH\7F\2\2H\f\3\2"+
		"\2\2IJ\7@\2\2JK\7?\2\2K\16\3\2\2\2LM\7]\2\2M\20\3\2\2\2NO\7>\2\2O\22\3"+
		"\2\2\2PQ\7?\2\2Q\24\3\2\2\2RS\7_\2\2S\26\3\2\2\2TU\7@\2\2U\30\3\2\2\2"+
		"VW\7>\2\2WX\7?\2\2X\32\3\2\2\2YZ\7C\2\2Z[\7n\2\2[\\\7n\2\2\\\34\3\2\2"+
		"\2]^\7C\2\2^_\7N\2\2_`\7N\2\2`\36\3\2\2\2ab\7H\2\2b \3\2\2\2cd\7*\2\2"+
		"d\"\3\2\2\2ef\7+\2\2f$\3\2\2\2gh\7)\2\2hi\7V\2\2ij\7)\2\2j&\3\2\2\2kl"+
		"\7F\2\2lm\7K\2\2mn\7U\2\2no\7V\2\2op\7C\2\2pq\7P\2\2qr\7E\2\2rs\7G\2\2"+
		"s(\3\2\2\2tu\7-\2\2u*\3\2\2\2vw\7.\2\2w,\3\2\2\2xy\7/\2\2y.\3\2\2\2z{"+
		"\7\60\2\2{\60\3\2\2\2|~\4\62;\2}|\3\2\2\2~\177\3\2\2\2\177}\3\2\2\2\177"+
		"\u0080\3\2\2\2\u0080\62\3\2\2\2\u0081\u0083\t\2\2\2\u0082\u0081\3\2\2"+
		"\2\u0083\u0087\3\2\2\2\u0084\u0086\t\3\2\2\u0085\u0084\3\2\2\2\u0086\u0089"+
		"\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\64\3\2\2\2\u0089"+
		"\u0087\3\2\2\2\u008a\u008c\t\4\2\2\u008b\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\3\2\2\2\u008f"+
		"\u0090\b\33\2\2\u0090\66\3\2\2\2\b\2\177\u0082\u0085\u0087\u008d\3\b\2"+
		"\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}