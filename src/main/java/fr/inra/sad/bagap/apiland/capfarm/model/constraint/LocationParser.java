// Generated from Location.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.capfarm.model.constraint;
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
		T__23=1, T__22=2, T__21=3, T__20=4, T__19=5, T__18=6, T__17=7, T__16=8, 
		T__15=9, T__14=10, T__13=11, T__12=12, T__11=13, T__10=14, T__9=15, T__8=16, 
		T__7=17, T__6=18, T__5=19, T__4=20, T__3=21, T__2=22, T__1=23, T__0=24, 
		INTEGER=25, ATTRIBUTE=26, WS=27;
	public static final String[] tokenNames = {
		"<INVALID>", "''F''", "'all'", "'XOR'", "'T'", "'AREA'", "'AND'", "'>='", 
		"'['", "'<'", "'='", "']'", "'>'", "'<='", "'All'", "'ALL'", "'F'", "'('", 
		"')'", "''T''", "'DISTANCE'", "'+'", "','", "'-'", "'.'", "INTEGER", "ATTRIBUTE", 
		"WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_localisation = 1, RULE_terme = 2, RULE_plusminus = 3, 
		RULE_parcelles = 4, RULE_boolatt = 5, RULE_numatt = 6, RULE_distance = 7, 
		RULE_area = 8, RULE_partout = 9, RULE_andterme = 10, RULE_xorterme = 11;
	public static final String[] ruleNames = {
		"evaluate", "localisation", "terme", "plusminus", "parcelles", "boolatt", 
		"numatt", "distance", "area", "partout", "andterme", "xorterme"
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
			setState(24); localisation();
			setState(25); match(EOF);
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
			setState(32);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(27); partout();
				}
				break;
			case 2:
				{
				setState(29);
				_la = _input.LA(1);
				if (_la==T__3 || _la==T__1) {
					{
					setState(28); plusminus();
					}
				}

				setState(31); terme();
				}
				break;
			}
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3 || _la==T__1) {
				{
				{
				setState(34); plusminus();
				setState(35); terme();
				}
				}
				setState(41);
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
		public AreaContext area() {
			return getRuleContext(AreaContext.class,0);
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
			setState(49);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(42); parcelles();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(43); boolatt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(44); numatt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(45); distance();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(46); area();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(47); andterme();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(48); xorterme();
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
			setState(51);
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
			setState(53); match(T__16);
			setState(54); match(INTEGER);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(55); match(T__2);
				setState(56); match(INTEGER);
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(62); match(T__13);
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
			setState(64); match(T__16);
			setState(65); match(ATTRIBUTE);
			setState(71);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(66); match(T__14);
				setState(69);
				switch (_input.LA(1)) {
				case T__20:
				case T__5:
					{
					setState(67);
					_la = _input.LA(1);
					if ( !(_la==T__20 || _la==T__5) ) {
					_errHandler.recoverInline(this);
					}
					consume();
					}
					break;
				case T__23:
				case T__8:
					{
					setState(68);
					_la = _input.LA(1);
					if ( !(_la==T__23 || _la==T__8) ) {
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

			setState(73); match(T__13);
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
			setState(75); match(T__16);
			setState(76); match(ATTRIBUTE);
			setState(77);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(78); match(INTEGER);
			setState(81);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(79); match(T__0);
				setState(80); match(INTEGER);
				}
			}

			setState(83); match(T__13);
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
			setState(85); match(T__4);
			setState(86); match(T__7);
			setState(87); match(ATTRIBUTE);
			setState(88);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(89); match(INTEGER);
			setState(92);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(90); match(T__0);
				setState(91); match(INTEGER);
				}
			}

			setState(94); match(T__6);
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

	public static class AreaContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(LocationParser.INTEGER, i);
		}
		public AreaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_area; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterArea(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitArea(this);
		}
	}

	public final AreaContext area() throws RecognitionException {
		AreaContext _localctx = new AreaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_area);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(T__19);
			setState(97);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(98); match(INTEGER);
			setState(101);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(99); match(T__0);
				setState(100); match(INTEGER);
				}
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
		enterRule(_localctx, 18, RULE_partout);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__22) | (1L << T__10) | (1L << T__9))) != 0)) ) {
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
		enterRule(_localctx, 20, RULE_andterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105); match(T__18);
			setState(106); match(T__7);
			setState(107); localisation();
			setState(108); match(T__2);
			setState(109); localisation();
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(110); match(T__2);
				setState(111); localisation();
				}
				}
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(117); match(T__6);
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
		enterRule(_localctx, 22, RULE_xorterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119); match(T__21);
			setState(120); match(T__7);
			setState(121); localisation();
			setState(122); match(T__2);
			setState(123); localisation();
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(124); match(T__2);
				setState(125); localisation();
				}
				}
				setState(130);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(131); match(T__6);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\35\u0088\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\2\3\3\3\3\5\3 \n\3\3\3\5\3#\n\3\3\3\3"+
		"\3\3\3\7\3(\n\3\f\3\16\3+\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\64\n\4"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\7\6<\n\6\f\6\16\6?\13\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\7\3\7\5\7H\n\7\5\7J\n\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bT\n\b\3\b"+
		"\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t_\n\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n"+
		"\5\nh\n\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\7\fs\n\f\f\f\16\fv\13"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\7\r\u0081\n\r\f\r\16\r\u0084\13"+
		"\r\3\r\3\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\7\4\2\27\27\31\31"+
		"\4\2\6\6\25\25\4\2\3\3\22\22\5\2\t\t\13\f\16\17\4\2\4\4\20\21\u008d\2"+
		"\32\3\2\2\2\4\"\3\2\2\2\6\63\3\2\2\2\b\65\3\2\2\2\n\67\3\2\2\2\fB\3\2"+
		"\2\2\16M\3\2\2\2\20W\3\2\2\2\22b\3\2\2\2\24i\3\2\2\2\26k\3\2\2\2\30y\3"+
		"\2\2\2\32\33\5\4\3\2\33\34\7\2\2\3\34\3\3\2\2\2\35#\5\24\13\2\36 \5\b"+
		"\5\2\37\36\3\2\2\2\37 \3\2\2\2 !\3\2\2\2!#\5\6\4\2\"\35\3\2\2\2\"\37\3"+
		"\2\2\2\"#\3\2\2\2#)\3\2\2\2$%\5\b\5\2%&\5\6\4\2&(\3\2\2\2\'$\3\2\2\2("+
		"+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\5\3\2\2\2+)\3\2\2\2,\64\5\n\6\2-\64\5"+
		"\f\7\2.\64\5\16\b\2/\64\5\20\t\2\60\64\5\22\n\2\61\64\5\26\f\2\62\64\5"+
		"\30\r\2\63,\3\2\2\2\63-\3\2\2\2\63.\3\2\2\2\63/\3\2\2\2\63\60\3\2\2\2"+
		"\63\61\3\2\2\2\63\62\3\2\2\2\64\7\3\2\2\2\65\66\t\2\2\2\66\t\3\2\2\2\67"+
		"8\7\n\2\28=\7\33\2\29:\7\30\2\2:<\7\33\2\2;9\3\2\2\2<?\3\2\2\2=;\3\2\2"+
		"\2=>\3\2\2\2>@\3\2\2\2?=\3\2\2\2@A\7\r\2\2A\13\3\2\2\2BC\7\n\2\2CI\7\34"+
		"\2\2DG\7\f\2\2EH\t\3\2\2FH\t\4\2\2GE\3\2\2\2GF\3\2\2\2HJ\3\2\2\2ID\3\2"+
		"\2\2IJ\3\2\2\2JK\3\2\2\2KL\7\r\2\2L\r\3\2\2\2MN\7\n\2\2NO\7\34\2\2OP\t"+
		"\5\2\2PS\7\33\2\2QR\7\32\2\2RT\7\33\2\2SQ\3\2\2\2ST\3\2\2\2TU\3\2\2\2"+
		"UV\7\r\2\2V\17\3\2\2\2WX\7\26\2\2XY\7\23\2\2YZ\7\34\2\2Z[\t\5\2\2[^\7"+
		"\33\2\2\\]\7\32\2\2]_\7\33\2\2^\\\3\2\2\2^_\3\2\2\2_`\3\2\2\2`a\7\24\2"+
		"\2a\21\3\2\2\2bc\7\7\2\2cd\t\5\2\2dg\7\33\2\2ef\7\32\2\2fh\7\33\2\2ge"+
		"\3\2\2\2gh\3\2\2\2h\23\3\2\2\2ij\t\6\2\2j\25\3\2\2\2kl\7\b\2\2lm\7\23"+
		"\2\2mn\5\4\3\2no\7\30\2\2ot\5\4\3\2pq\7\30\2\2qs\5\4\3\2rp\3\2\2\2sv\3"+
		"\2\2\2tr\3\2\2\2tu\3\2\2\2uw\3\2\2\2vt\3\2\2\2wx\7\24\2\2x\27\3\2\2\2"+
		"yz\7\5\2\2z{\7\23\2\2{|\5\4\3\2|}\7\30\2\2}\u0082\5\4\3\2~\177\7\30\2"+
		"\2\177\u0081\5\4\3\2\u0080~\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3"+
		"\2\2\2\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\u0082\3\2\2\2\u0085"+
		"\u0086\7\24\2\2\u0086\31\3\2\2\2\16\37\")\63=GIS^gt\u0082";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}