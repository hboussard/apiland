// Generated from Location.g4 by ANTLR 4.4
package fr.inra.sad_paysage.apiland.capfarm.model.constraint;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LocationParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__22=1, T__21=2, T__20=3, T__19=4, T__18=5, T__17=6, T__16=7, T__15=8, 
		T__14=9, T__13=10, T__12=11, T__11=12, T__10=13, T__9=14, T__8=15, T__7=16, 
		T__6=17, T__5=18, T__4=19, T__3=20, T__2=21, T__1=22, T__0=23, INTEGER=24, 
		ATTRIBUTE=25, WS=26;
	public static final String[] tokenNames = {
		"<INVALID>", "''F''", "'all'", "'XOR'", "'T'", "'AND'", "'>='", "'['", 
		"'<'", "'='", "']'", "'>'", "'<='", "'All'", "'ALL'", "'F'", "'('", "')'", 
		"''T''", "'DISTANCE'", "'+'", "','", "'-'", "'.'", "INTEGER", "ATTRIBUTE", 
		"WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_localisation = 1, RULE_terme = 2, RULE_plusminus = 3, 
		RULE_parcelles = 4, RULE_boolatt = 5, RULE_numatt = 6, RULE_distance = 7, 
		RULE_partout = 8, RULE_andterme = 9, RULE_xorterme = 10;
	public static final String[] ruleNames = {
		"evaluate", "localisation", "terme", "plusminus", "parcelles", "boolatt", 
		"numatt", "distance", "partout", "andterme", "xorterme"
	};

	@Override
	public String getGrammarFileName() { return "Location.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LocationParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class EvaluateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LocationParser.EOF, 0); }
		public LocalisationContext localisation() {
			return getRuleContext(LocalisationContext.class,0);
		}
		public EvaluateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterEvaluate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitEvaluate(this);
		}
	}

	public final EvaluateContext evaluate() throws RecognitionException {
		EvaluateContext _localctx = new EvaluateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_evaluate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22); localisation();
			setState(23); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocalisationContext extends ParserRuleContext {
		public List<TermeContext> terme() {
			return getRuleContexts(TermeContext.class);
		}
		public PartoutContext partout() {
			return getRuleContext(PartoutContext.class,0);
		}
		public List<PlusminusContext> plusminus() {
			return getRuleContexts(PlusminusContext.class);
		}
		public PlusminusContext plusminus(int i) {
			return getRuleContext(PlusminusContext.class,i);
		}
		public TermeContext terme(int i) {
			return getRuleContext(TermeContext.class,i);
		}
		public LocalisationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localisation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterLocalisation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitLocalisation(this);
		}
	}

	public final LocalisationContext localisation() throws RecognitionException {
		LocalisationContext _localctx = new LocalisationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_localisation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(25); partout();
				}
				break;
			case 2:
				{
				setState(27);
				_la = _input.LA(1);
				if (_la==T__3 || _la==T__1) {
					{
					setState(26); plusminus();
					}
				}

				setState(29); terme();
				}
				break;
			}
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3 || _la==T__1) {
				{
				{
				setState(32); plusminus();
				setState(33); terme();
				}
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermeContext extends ParserRuleContext {
		public NumattContext numatt() {
			return getRuleContext(NumattContext.class,0);
		}
		public DistanceContext distance() {
			return getRuleContext(DistanceContext.class,0);
		}
		public XortermeContext xorterme() {
			return getRuleContext(XortermeContext.class,0);
		}
		public BoolattContext boolatt() {
			return getRuleContext(BoolattContext.class,0);
		}
		public AndtermeContext andterme() {
			return getRuleContext(AndtermeContext.class,0);
		}
		public ParcellesContext parcelles() {
			return getRuleContext(ParcellesContext.class,0);
		}
		public TermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_terme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterTerme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitTerme(this);
		}
	}

	public final TermeContext terme() throws RecognitionException {
		TermeContext _localctx = new TermeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_terme);
		try {
			setState(46);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(40); parcelles();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(41); boolatt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(42); numatt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(43); distance();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(44); andterme();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(45); xorterme();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PlusminusContext extends ParserRuleContext {
		public PlusminusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plusminus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterPlusminus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitPlusminus(this);
		}
	}

	public final PlusminusContext plusminus() throws RecognitionException {
		PlusminusContext _localctx = new PlusminusContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_plusminus);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			_la = _input.LA(1);
			if ( !(_la==T__3 || _la==T__1) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParcellesContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public ParcellesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parcelles; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterParcelles(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitParcelles(this);
		}
	}

	public final ParcellesContext parcelles() throws RecognitionException {
		ParcellesContext _localctx = new ParcellesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_parcelles);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50); match(T__16);
			setState(51); match(INTEGER);
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(52); match(T__2);
				setState(53); match(INTEGER);
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59); match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolattContext extends ParserRuleContext {
		public TerminalNode ATTRIBUTE() { return getToken(LocationParser.ATTRIBUTE, 0); }
		public BoolattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterBoolatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitBoolatt(this);
		}
	}

	public final BoolattContext boolatt() throws RecognitionException {
		BoolattContext _localctx = new BoolattContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_boolatt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); match(T__16);
			setState(62); match(ATTRIBUTE);
			setState(68);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(63); match(T__14);
				setState(66);
				switch (_input.LA(1)) {
				case T__19:
				case T__5:
					{
					setState(64);
					_la = _input.LA(1);
					if ( !(_la==T__19 || _la==T__5) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					break;
				case T__22:
				case T__8:
					{
					setState(65);
					_la = _input.LA(1);
					if ( !(_la==T__22 || _la==T__8) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
			}

			setState(70); match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumattContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode ATTRIBUTE() { return getToken(LocationParser.ATTRIBUTE, 0); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public NumattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterNumatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitNumatt(this);
		}
	}

	public final NumattContext numatt() throws RecognitionException {
		NumattContext _localctx = new NumattContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_numatt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); match(T__16);
			setState(73); match(ATTRIBUTE);
			setState(74);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(75); match(INTEGER);
			setState(78);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(76); match(T__0);
				setState(77); match(INTEGER);
				}
			}

			setState(80); match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DistanceContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode ATTRIBUTE() { return getToken(LocationParser.ATTRIBUTE, 0); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public DistanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_distance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterDistance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitDistance(this);
		}
	}

	public final DistanceContext distance() throws RecognitionException {
		DistanceContext _localctx = new DistanceContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_distance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); match(T__4);
			setState(83); match(T__7);
			setState(84); match(ATTRIBUTE);
			setState(85);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(86); match(INTEGER);
			setState(89);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(87); match(T__0);
				setState(88); match(INTEGER);
				}
			}

			setState(91); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartoutContext extends ParserRuleContext {
		public PartoutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partout; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterPartout(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitPartout(this);
		}
	}

	public final PartoutContext partout() throws RecognitionException {
		PartoutContext _localctx = new PartoutContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_partout);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__21) | (1L << T__10) | (1L << T__9))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndtermeContext extends ParserRuleContext {
		public LocalisationContext localisation(int i) {
			return getRuleContext(LocalisationContext.class,i);
		}
		public List<LocalisationContext> localisation() {
			return getRuleContexts(LocalisationContext.class);
		}
		public AndtermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andterme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterAndterme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitAndterme(this);
		}
	}

	public final AndtermeContext andterme() throws RecognitionException {
		AndtermeContext _localctx = new AndtermeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_andterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); match(T__18);
			setState(96); match(T__7);
			setState(97); localisation();
			setState(98); match(T__2);
			setState(99); localisation();
			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(100); match(T__2);
				setState(101); localisation();
				}
				}
				setState(106);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(107); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XortermeContext extends ParserRuleContext {
		public LocalisationContext localisation(int i) {
			return getRuleContext(LocalisationContext.class,i);
		}
		public List<LocalisationContext> localisation() {
			return getRuleContexts(LocalisationContext.class);
		}
		public XortermeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xorterme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterXorterme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitXorterme(this);
		}
	}

	public final XortermeContext xorterme() throws RecognitionException {
		XortermeContext _localctx = new XortermeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_xorterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109); match(T__20);
			setState(110); match(T__7);
			setState(111); localisation();
			setState(112); match(T__2);
			setState(113); localisation();
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(114); match(T__2);
				setState(115); localisation();
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(121); match(T__6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\34~\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\3\2\3\2\3\3\3\3\5\3\36\n\3\3\3\5\3!\n\3\3\3\3\3\3\3\7\3&\n"+
		"\3\f\3\16\3)\13\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4\61\n\4\3\5\3\5\3\6\3\6\3"+
		"\6\3\6\7\69\n\6\f\6\16\6<\13\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\5\7E\n\7\5"+
		"\7G\n\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bQ\n\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\5\t\\\n\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\7\13i\n\13\f\13\16\13l\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\7\fw\n\f\f\f\16\fz\13\f\3\f\3\f\3\f\2\2\r\2\4\6\b\n\f\16\20\22\24\26"+
		"\2\7\4\2\26\26\30\30\4\2\6\6\24\24\4\2\3\3\21\21\5\2\b\b\n\13\r\16\4\2"+
		"\4\4\17\20\u0082\2\30\3\2\2\2\4 \3\2\2\2\6\60\3\2\2\2\b\62\3\2\2\2\n\64"+
		"\3\2\2\2\f?\3\2\2\2\16J\3\2\2\2\20T\3\2\2\2\22_\3\2\2\2\24a\3\2\2\2\26"+
		"o\3\2\2\2\30\31\5\4\3\2\31\32\7\2\2\3\32\3\3\2\2\2\33!\5\22\n\2\34\36"+
		"\5\b\5\2\35\34\3\2\2\2\35\36\3\2\2\2\36\37\3\2\2\2\37!\5\6\4\2 \33\3\2"+
		"\2\2 \35\3\2\2\2 !\3\2\2\2!\'\3\2\2\2\"#\5\b\5\2#$\5\6\4\2$&\3\2\2\2%"+
		"\"\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'(\3\2\2\2(\5\3\2\2\2)\'\3\2\2\2*\61\5"+
		"\n\6\2+\61\5\f\7\2,\61\5\16\b\2-\61\5\20\t\2.\61\5\24\13\2/\61\5\26\f"+
		"\2\60*\3\2\2\2\60+\3\2\2\2\60,\3\2\2\2\60-\3\2\2\2\60.\3\2\2\2\60/\3\2"+
		"\2\2\61\7\3\2\2\2\62\63\t\2\2\2\63\t\3\2\2\2\64\65\7\t\2\2\65:\7\32\2"+
		"\2\66\67\7\27\2\2\679\7\32\2\28\66\3\2\2\29<\3\2\2\2:8\3\2\2\2:;\3\2\2"+
		"\2;=\3\2\2\2<:\3\2\2\2=>\7\f\2\2>\13\3\2\2\2?@\7\t\2\2@F\7\33\2\2AD\7"+
		"\13\2\2BE\t\3\2\2CE\t\4\2\2DB\3\2\2\2DC\3\2\2\2EG\3\2\2\2FA\3\2\2\2FG"+
		"\3\2\2\2GH\3\2\2\2HI\7\f\2\2I\r\3\2\2\2JK\7\t\2\2KL\7\33\2\2LM\t\5\2\2"+
		"MP\7\32\2\2NO\7\31\2\2OQ\7\32\2\2PN\3\2\2\2PQ\3\2\2\2QR\3\2\2\2RS\7\f"+
		"\2\2S\17\3\2\2\2TU\7\25\2\2UV\7\22\2\2VW\7\33\2\2WX\t\5\2\2X[\7\32\2\2"+
		"YZ\7\31\2\2Z\\\7\32\2\2[Y\3\2\2\2[\\\3\2\2\2\\]\3\2\2\2]^\7\23\2\2^\21"+
		"\3\2\2\2_`\t\6\2\2`\23\3\2\2\2ab\7\7\2\2bc\7\22\2\2cd\5\4\3\2de\7\27\2"+
		"\2ej\5\4\3\2fg\7\27\2\2gi\5\4\3\2hf\3\2\2\2il\3\2\2\2jh\3\2\2\2jk\3\2"+
		"\2\2km\3\2\2\2lj\3\2\2\2mn\7\23\2\2n\25\3\2\2\2op\7\5\2\2pq\7\22\2\2q"+
		"r\5\4\3\2rs\7\27\2\2sx\5\4\3\2tu\7\27\2\2uw\5\4\3\2vt\3\2\2\2wz\3\2\2"+
		"\2xv\3\2\2\2xy\3\2\2\2y{\3\2\2\2zx\3\2\2\2{|\7\23\2\2|\27\3\2\2\2\r\35"+
		" \'\60:DFP[jx";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}