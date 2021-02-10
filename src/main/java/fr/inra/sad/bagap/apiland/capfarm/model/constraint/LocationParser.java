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
		INTEGER=25, TEXT=26, WS=27;
	public static final String[] tokenNames = {
		"<INVALID>", "''F''", "'all'", "'XOR'", "'T'", "'AREA'", "'AND'", "'>='", 
		"'['", "'<'", "'='", "']'", "'>'", "'<='", "'All'", "'ALL'", "'F'", "'('", 
		"')'", "''T''", "'DISTANCE'", "'+'", "','", "'-'", "'.'", "INTEGER", "TEXT", 
		"WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_localisation = 1, RULE_terme = 2, RULE_plusminus = 3, 
		RULE_parcelles = 4, RULE_boolatt = 5, RULE_stringatt = 6, RULE_numatt = 7, 
		RULE_distance = 8, RULE_area = 9, RULE_partout = 10, RULE_andterme = 11, 
		RULE_xorterme = 12;
	public static final String[] ruleNames = {
		"evaluate", "localisation", "terme", "plusminus", "parcelles", "boolatt", 
		"stringatt", "numatt", "distance", "area", "partout", "andterme", "xorterme"
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
			setState(26); localisation();
			setState(27); match(EOF);
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
			setState(34);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(29); partout();
				}
				break;
			case 2:
				{
				setState(31);
				_la = _input.LA(1);
				if (_la==T__3 || _la==T__1) {
					{
					setState(30); plusminus();
					}
				}

				setState(33); terme();
				}
				break;
			}
			setState(41);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3 || _la==T__1) {
				{
				{
				setState(36); plusminus();
				setState(37); terme();
				}
				}
				setState(43);
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
		public StringattContext stringatt() {
			return getRuleContext(StringattContext.class,0);
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
			setState(52);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(44); parcelles();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(45); boolatt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(46); stringatt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(47); numatt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(48); distance();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(49); area();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(50); andterme();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(51); xorterme();
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
			setState(54);
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
			setState(56); match(T__16);
			setState(57); match(INTEGER);
			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(58); match(T__2);
				setState(59); match(INTEGER);
				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(65); match(T__13);
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
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
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
			setState(67); match(T__16);
			setState(68); match(TEXT);
			setState(74);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(69); match(T__14);
				setState(72);
				switch (_input.LA(1)) {
				case T__20:
				case T__5:
					{
					setState(70);
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
					setState(71);
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

			setState(76); match(T__13);
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

	public static class StringattContext extends ParserRuleContext {
		public List<TerminalNode> TEXT() { return getTokens(LocationParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(LocationParser.TEXT, i);
		}
		public StringattContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringatt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).enterStringatt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LocationListener ) ((LocationListener)listener).exitStringatt(this);
		}
	}

	public final StringattContext stringatt() throws RecognitionException {
		StringattContext _localctx = new StringattContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_stringatt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78); match(T__16);
			setState(79); match(TEXT);
			setState(80); match(T__14);
			setState(81); match(TEXT);
			setState(82); match(T__13);
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
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
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
		enterRule(_localctx, 14, RULE_numatt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84); match(T__16);
			setState(85); match(TEXT);
			setState(86);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(87); match(INTEGER);
			setState(90);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(88); match(T__0);
				setState(89); match(INTEGER);
				}
			}

			setState(92); match(T__13);
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
		public TerminalNode TEXT() { return getToken(LocationParser.TEXT, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(LocationParser.INTEGER); }
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
		enterRule(_localctx, 16, RULE_distance);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94); match(T__4);
			setState(95); match(T__7);
			setState(96); match(TEXT);
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

			setState(103); match(T__6);
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
		enterRule(_localctx, 18, RULE_area);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105); match(T__19);
			setState(106);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__15) | (1L << T__14) | (1L << T__12) | (1L << T__11))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(107); match(INTEGER);
			setState(110);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(108); match(T__0);
				setState(109); match(INTEGER);
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
		enterRule(_localctx, 20, RULE_partout);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
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
		enterRule(_localctx, 22, RULE_andterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114); match(T__18);
			setState(115); match(T__7);
			setState(116); localisation();
			setState(117); match(T__2);
			setState(118); localisation();
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(119); match(T__2);
				setState(120); localisation();
				}
				}
				setState(125);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(126); match(T__6);
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
		enterRule(_localctx, 24, RULE_xorterme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); match(T__21);
			setState(129); match(T__7);
			setState(130); localisation();
			setState(131); match(T__2);
			setState(132); localisation();
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(133); match(T__2);
				setState(134); localisation();
				}
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(140); match(T__6);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\35\u0091\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\3\3\5\3\"\n\3\3\3\5\3"+
		"%\n\3\3\3\3\3\3\3\7\3*\n\3\f\3\16\3-\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\5\4\67\n\4\3\5\3\5\3\6\3\6\3\6\3\6\7\6?\n\6\f\6\16\6B\13\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\7\5\7K\n\7\5\7M\n\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\t\3\t\5\t]\n\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\5\nh\n\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\5\13q\n\13\3\f\3\f\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\7\r|\n\r\f\r\16\r\177\13\r\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\7\16\u008a\n\16\f\16\16\16\u008d\13\16\3\16\3"+
		"\16\3\16\2\2\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\7\4\2\27\27\31\31\4"+
		"\2\6\6\25\25\4\2\3\3\22\22\5\2\t\t\13\f\16\17\4\2\4\4\20\21\u0096\2\34"+
		"\3\2\2\2\4$\3\2\2\2\6\66\3\2\2\2\b8\3\2\2\2\n:\3\2\2\2\fE\3\2\2\2\16P"+
		"\3\2\2\2\20V\3\2\2\2\22`\3\2\2\2\24k\3\2\2\2\26r\3\2\2\2\30t\3\2\2\2\32"+
		"\u0082\3\2\2\2\34\35\5\4\3\2\35\36\7\2\2\3\36\3\3\2\2\2\37%\5\26\f\2 "+
		"\"\5\b\5\2! \3\2\2\2!\"\3\2\2\2\"#\3\2\2\2#%\5\6\4\2$\37\3\2\2\2$!\3\2"+
		"\2\2$%\3\2\2\2%+\3\2\2\2&\'\5\b\5\2\'(\5\6\4\2(*\3\2\2\2)&\3\2\2\2*-\3"+
		"\2\2\2+)\3\2\2\2+,\3\2\2\2,\5\3\2\2\2-+\3\2\2\2.\67\5\n\6\2/\67\5\f\7"+
		"\2\60\67\5\16\b\2\61\67\5\20\t\2\62\67\5\22\n\2\63\67\5\24\13\2\64\67"+
		"\5\30\r\2\65\67\5\32\16\2\66.\3\2\2\2\66/\3\2\2\2\66\60\3\2\2\2\66\61"+
		"\3\2\2\2\66\62\3\2\2\2\66\63\3\2\2\2\66\64\3\2\2\2\66\65\3\2\2\2\67\7"+
		"\3\2\2\289\t\2\2\29\t\3\2\2\2:;\7\n\2\2;@\7\33\2\2<=\7\30\2\2=?\7\33\2"+
		"\2><\3\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2AC\3\2\2\2B@\3\2\2\2CD\7\r\2"+
		"\2D\13\3\2\2\2EF\7\n\2\2FL\7\34\2\2GJ\7\f\2\2HK\t\3\2\2IK\t\4\2\2JH\3"+
		"\2\2\2JI\3\2\2\2KM\3\2\2\2LG\3\2\2\2LM\3\2\2\2MN\3\2\2\2NO\7\r\2\2O\r"+
		"\3\2\2\2PQ\7\n\2\2QR\7\34\2\2RS\7\f\2\2ST\7\34\2\2TU\7\r\2\2U\17\3\2\2"+
		"\2VW\7\n\2\2WX\7\34\2\2XY\t\5\2\2Y\\\7\33\2\2Z[\7\32\2\2[]\7\33\2\2\\"+
		"Z\3\2\2\2\\]\3\2\2\2]^\3\2\2\2^_\7\r\2\2_\21\3\2\2\2`a\7\26\2\2ab\7\23"+
		"\2\2bc\7\34\2\2cd\t\5\2\2dg\7\33\2\2ef\7\32\2\2fh\7\33\2\2ge\3\2\2\2g"+
		"h\3\2\2\2hi\3\2\2\2ij\7\24\2\2j\23\3\2\2\2kl\7\7\2\2lm\t\5\2\2mp\7\33"+
		"\2\2no\7\32\2\2oq\7\33\2\2pn\3\2\2\2pq\3\2\2\2q\25\3\2\2\2rs\t\6\2\2s"+
		"\27\3\2\2\2tu\7\b\2\2uv\7\23\2\2vw\5\4\3\2wx\7\30\2\2x}\5\4\3\2yz\7\30"+
		"\2\2z|\5\4\3\2{y\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0080\3\2\2"+
		"\2\177}\3\2\2\2\u0080\u0081\7\24\2\2\u0081\31\3\2\2\2\u0082\u0083\7\5"+
		"\2\2\u0083\u0084\7\23\2\2\u0084\u0085\5\4\3\2\u0085\u0086\7\30\2\2\u0086"+
		"\u008b\5\4\3\2\u0087\u0088\7\30\2\2\u0088\u008a\5\4\3\2\u0089\u0087\3"+
		"\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008e\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u008f\7\24\2\2\u008f\33\3\2\2"+
		"\2\16!$+\66@JL\\gp}\u008b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}