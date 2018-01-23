// Generated from Combination.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.analysis.matrix.pixel;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CombinationParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__25=1, T__24=2, T__23=3, T__22=4, T__21=5, T__20=6, T__19=7, T__18=8, 
		T__17=9, T__16=10, T__15=11, T__14=12, T__13=13, T__12=14, T__11=15, T__10=16, 
		T__9=17, T__8=18, T__7=19, T__6=20, T__5=21, T__4=22, T__3=23, T__2=24, 
		T__1=25, T__0=26, NUMBER=27, NAME=28, NODATA_Value=29, WS=30;
	public static final String[] tokenNames = {
		"<INVALID>", "'/'", "'!='", "'AND'", "'{'", "'&&'", "'}'", "'if'", "'<='", 
		"'pow'", "'&'", "'\\|\\|'", "'('", "'*'", "','", "'>='", "'log'", "'=='", 
		"'<'", "'>'", "'OR'", "'else'", "')'", "'exp'", "'+'", "'\\|'", "'-'", 
		"NUMBER", "NAME", "'NODATA_Value'", "WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_combination = 1, RULE_condition = 2, RULE_ifcombination = 3, 
		RULE_elsecombination = 4, RULE_logicalop = 5, RULE_boolterm = 6, RULE_boolop = 7, 
		RULE_operation = 8, RULE_leftoperation = 9, RULE_rightoperation = 10, 
		RULE_term = 11, RULE_termminus = 12, RULE_termwithcoma = 13, RULE_matrix = 14, 
		RULE_mathop = 15, RULE_function = 16, RULE_function1param = 17, RULE_function2params = 18;
	public static final String[] ruleNames = {
		"evaluate", "combination", "condition", "ifcombination", "elsecombination", 
		"logicalop", "boolterm", "boolop", "operation", "leftoperation", "rightoperation", 
		"term", "termminus", "termwithcoma", "matrix", "mathop", "function", "function1param", 
		"function2params"
	};

	@Override
	public String getGrammarFileName() { return "Combination.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CombinationParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class EvaluateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CombinationParser.EOF, 0); }
		public CombinationContext combination() {
			return getRuleContext(CombinationContext.class,0);
		}
		public EvaluateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_evaluate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterEvaluate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitEvaluate(this);
		}
	}

	public final EvaluateContext evaluate() throws RecognitionException {
		EvaluateContext _localctx = new EvaluateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_evaluate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); combination();
			setState(39); match(EOF);
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

	public static class CombinationContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public CombinationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_combination; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterCombination(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitCombination(this);
		}
	}

	public final CombinationContext combination() throws RecognitionException {
		CombinationContext _localctx = new CombinationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_combination);
		try {
			setState(43);
			switch (_input.LA(1)) {
			case T__19:
				enterOuterAlt(_localctx, 1);
				{
				setState(41); condition();
				}
				break;
			case T__17:
			case T__14:
			case T__10:
			case T__3:
			case T__0:
			case NUMBER:
			case NAME:
			case NODATA_Value:
				enterOuterAlt(_localctx, 2);
				{
				setState(42); operation();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class ConditionContext extends ParserRuleContext {
		public IfcombinationContext ifcombination() {
			return getRuleContext(IfcombinationContext.class,0);
		}
		public ElsecombinationContext elsecombination() {
			return getRuleContext(ElsecombinationContext.class,0);
		}
		public BooltermContext boolterm() {
			return getRuleContext(BooltermContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45); match(T__19);
			setState(46); match(T__14);
			setState(47); boolterm();
			setState(48); match(T__4);
			setState(49); match(T__22);
			setState(50); ifcombination();
			setState(51); match(T__20);
			setState(57);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(52); match(T__5);
				setState(53); match(T__22);
				setState(54); elsecombination();
				setState(55); match(T__20);
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

	public static class IfcombinationContext extends ParserRuleContext {
		public CombinationContext combination() {
			return getRuleContext(CombinationContext.class,0);
		}
		public IfcombinationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifcombination; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterIfcombination(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitIfcombination(this);
		}
	}

	public final IfcombinationContext ifcombination() throws RecognitionException {
		IfcombinationContext _localctx = new IfcombinationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_ifcombination);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); combination();
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

	public static class ElsecombinationContext extends ParserRuleContext {
		public CombinationContext combination() {
			return getRuleContext(CombinationContext.class,0);
		}
		public ElsecombinationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elsecombination; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterElsecombination(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitElsecombination(this);
		}
	}

	public final ElsecombinationContext elsecombination() throws RecognitionException {
		ElsecombinationContext _localctx = new ElsecombinationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_elsecombination);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); combination();
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

	public static class LogicalopContext extends ParserRuleContext {
		public LogicalopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterLogicalop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitLogicalop(this);
		}
	}

	public final LogicalopContext logicalop() throws RecognitionException {
		LogicalopContext _localctx = new LogicalopContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_logicalop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__21) | (1L << T__16) | (1L << T__15) | (1L << T__6) | (1L << T__1))) != 0)) ) {
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

	public static class BooltermContext extends ParserRuleContext {
		public LeftoperationContext leftoperation() {
			return getRuleContext(LeftoperationContext.class,0);
		}
		public RightoperationContext rightoperation() {
			return getRuleContext(RightoperationContext.class,0);
		}
		public BooltermContext boolterm() {
			return getRuleContext(BooltermContext.class,0);
		}
		public BoolopContext boolop() {
			return getRuleContext(BoolopContext.class,0);
		}
		public LogicalopContext logicalop() {
			return getRuleContext(LogicalopContext.class,0);
		}
		public BooltermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterBoolterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitBoolterm(this);
		}
	}

	public final BooltermContext boolterm() throws RecognitionException {
		BooltermContext _localctx = new BooltermContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_boolterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65); leftoperation();
			setState(66); boolop();
			setState(67); rightoperation();
			setState(71);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__21) | (1L << T__16) | (1L << T__15) | (1L << T__6) | (1L << T__1))) != 0)) {
				{
				setState(68); logicalop();
				setState(69); boolterm();
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

	public static class BoolopContext extends ParserRuleContext {
		public BoolopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterBoolop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitBoolop(this);
		}
	}

	public final BoolopContext boolop() throws RecognitionException {
		BoolopContext _localctx = new BoolopContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_boolop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__24) | (1L << T__18) | (1L << T__11) | (1L << T__9) | (1L << T__8) | (1L << T__7))) != 0)) ) {
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

	public static class OperationContext extends ParserRuleContext {
		public MathopContext mathop(int i) {
			return getRuleContext(MathopContext.class,i);
		}
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<MathopContext> mathop() {
			return getRuleContexts(MathopContext.class);
		}
		public OperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterOperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitOperation(this);
		}
	}

	public final OperationContext operation() throws RecognitionException {
		OperationContext _localctx = new OperationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_operation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75); term();
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__25) | (1L << T__13) | (1L << T__2) | (1L << T__0))) != 0)) {
				{
				{
				setState(76); mathop();
				setState(77); term();
				}
				}
				setState(83);
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

	public static class LeftoperationContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public LeftoperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leftoperation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterLeftoperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitLeftoperation(this);
		}
	}

	public final LeftoperationContext leftoperation() throws RecognitionException {
		LeftoperationContext _localctx = new LeftoperationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_leftoperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84); operation();
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

	public static class RightoperationContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public RightoperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rightoperation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterRightoperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitRightoperation(this);
		}
	}

	public final RightoperationContext rightoperation() throws RecognitionException {
		RightoperationContext _localctx = new RightoperationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_rightoperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86); operation();
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

	public static class TermContext extends ParserRuleContext {
		public MatrixContext matrix() {
			return getRuleContext(MatrixContext.class,0);
		}
		public TermminusContext termminus() {
			return getRuleContext(TermminusContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(CombinationParser.NUMBER, 0); }
		public TerminalNode NODATA_Value() { return getToken(CombinationParser.NODATA_Value, 0); }
		public TermwithcomaContext termwithcoma() {
			return getRuleContext(TermwithcomaContext.class,0);
		}
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_term);
		try {
			setState(94);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(88); termwithcoma();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(89); termminus();
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 3);
				{
				setState(90); match(NUMBER);
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 4);
				{
				setState(91); matrix();
				}
				break;
			case T__17:
			case T__10:
			case T__3:
				enterOuterAlt(_localctx, 5);
				{
				setState(92); function();
				}
				break;
			case NODATA_Value:
				enterOuterAlt(_localctx, 6);
				{
				setState(93); match(NODATA_Value);
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class TermminusContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermminusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termminus; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterTermminus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitTermminus(this);
		}
	}

	public final TermminusContext termminus() throws RecognitionException {
		TermminusContext _localctx = new TermminusContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_termminus);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(T__0);
			setState(97); term();
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

	public static class TermwithcomaContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermwithcomaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termwithcoma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterTermwithcoma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitTermwithcoma(this);
		}
	}

	public final TermwithcomaContext termwithcoma() throws RecognitionException {
		TermwithcomaContext _localctx = new TermwithcomaContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_termwithcoma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99); match(T__14);
			setState(100); term();
			setState(101); match(T__4);
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

	public static class MatrixContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(CombinationParser.NAME, 0); }
		public MatrixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_matrix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterMatrix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitMatrix(this);
		}
	}

	public final MatrixContext matrix() throws RecognitionException {
		MatrixContext _localctx = new MatrixContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_matrix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103); match(NAME);
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

	public static class MathopContext extends ParserRuleContext {
		public MathopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mathop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterMathop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitMathop(this);
		}
	}

	public final MathopContext mathop() throws RecognitionException {
		MathopContext _localctx = new MathopContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_mathop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__25) | (1L << T__13) | (1L << T__2) | (1L << T__0))) != 0)) ) {
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

	public static class FunctionContext extends ParserRuleContext {
		public Function1paramContext function1param() {
			return getRuleContext(Function1paramContext.class,0);
		}
		public Function2paramsContext function2params() {
			return getRuleContext(Function2paramsContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitFunction(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_function);
		try {
			setState(109);
			switch (_input.LA(1)) {
			case T__10:
			case T__3:
				enterOuterAlt(_localctx, 1);
				{
				setState(107); function1param();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(108); function2params();
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class Function1paramContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public Function1paramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function1param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterFunction1param(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitFunction1param(this);
		}
	}

	public final Function1paramContext function1param() throws RecognitionException {
		Function1paramContext _localctx = new Function1paramContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_function1param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			_la = _input.LA(1);
			if ( !(_la==T__10 || _la==T__3) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(112); match(T__14);
			setState(113); operation();
			setState(114); match(T__4);
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

	public static class Function2paramsContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(CombinationParser.NUMBER, 0); }
		public Function2paramsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function2params; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterFunction2params(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitFunction2params(this);
		}
	}

	public final Function2paramsContext function2params() throws RecognitionException {
		Function2paramsContext _localctx = new Function2paramsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_function2params);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116); match(T__17);
			setState(117); match(T__14);
			setState(118); operation();
			setState(119); match(T__12);
			setState(120); match(NUMBER);
			setState(121); match(T__4);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3 ~\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f"+
		"\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\4\23\t"+
		"\23\4\24\t\24\3\2\3\2\3\2\3\3\3\3\5\3.\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\5\4<\n\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\5\bJ\n\b\3\t\3\t\3\n\3\n\3\n\3\n\7\nR\n\n\f\n\16\nU\13\n\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\5\ra\n\r\3\16\3\16\3\16\3\17\3\17"+
		"\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\5\22p\n\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\2\2\25\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&\2\6\7\2\5\5\7\7\f\r\26\26\33\33\6\2\4\4\n\n"+
		"\21\21\23\25\6\2\3\3\17\17\32\32\34\34\4\2\22\22\31\31t\2(\3\2\2\2\4-"+
		"\3\2\2\2\6/\3\2\2\2\b=\3\2\2\2\n?\3\2\2\2\fA\3\2\2\2\16C\3\2\2\2\20K\3"+
		"\2\2\2\22M\3\2\2\2\24V\3\2\2\2\26X\3\2\2\2\30`\3\2\2\2\32b\3\2\2\2\34"+
		"e\3\2\2\2\36i\3\2\2\2 k\3\2\2\2\"o\3\2\2\2$q\3\2\2\2&v\3\2\2\2()\5\4\3"+
		"\2)*\7\2\2\3*\3\3\2\2\2+.\5\6\4\2,.\5\22\n\2-+\3\2\2\2-,\3\2\2\2.\5\3"+
		"\2\2\2/\60\7\t\2\2\60\61\7\16\2\2\61\62\5\16\b\2\62\63\7\30\2\2\63\64"+
		"\7\6\2\2\64\65\5\b\5\2\65;\7\b\2\2\66\67\7\27\2\2\678\7\6\2\289\5\n\6"+
		"\29:\7\b\2\2:<\3\2\2\2;\66\3\2\2\2;<\3\2\2\2<\7\3\2\2\2=>\5\4\3\2>\t\3"+
		"\2\2\2?@\5\4\3\2@\13\3\2\2\2AB\t\2\2\2B\r\3\2\2\2CD\5\24\13\2DE\5\20\t"+
		"\2EI\5\26\f\2FG\5\f\7\2GH\5\16\b\2HJ\3\2\2\2IF\3\2\2\2IJ\3\2\2\2J\17\3"+
		"\2\2\2KL\t\3\2\2L\21\3\2\2\2MS\5\30\r\2NO\5 \21\2OP\5\30\r\2PR\3\2\2\2"+
		"QN\3\2\2\2RU\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\23\3\2\2\2US\3\2\2\2VW\5\22"+
		"\n\2W\25\3\2\2\2XY\5\22\n\2Y\27\3\2\2\2Za\5\34\17\2[a\5\32\16\2\\a\7\35"+
		"\2\2]a\5\36\20\2^a\5\"\22\2_a\7\37\2\2`Z\3\2\2\2`[\3\2\2\2`\\\3\2\2\2"+
		"`]\3\2\2\2`^\3\2\2\2`_\3\2\2\2a\31\3\2\2\2bc\7\34\2\2cd\5\30\r\2d\33\3"+
		"\2\2\2ef\7\16\2\2fg\5\30\r\2gh\7\30\2\2h\35\3\2\2\2ij\7\36\2\2j\37\3\2"+
		"\2\2kl\t\4\2\2l!\3\2\2\2mp\5$\23\2np\5&\24\2om\3\2\2\2on\3\2\2\2p#\3\2"+
		"\2\2qr\t\5\2\2rs\7\16\2\2st\5\22\n\2tu\7\30\2\2u%\3\2\2\2vw\7\13\2\2w"+
		"x\7\16\2\2xy\5\22\n\2yz\7\20\2\2z{\7\35\2\2{|\7\30\2\2|\'\3\2\2\2\b-;"+
		"IS`o";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}