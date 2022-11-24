// Generated from Combination.g4 by ANTLR 4.4
package fr.inra.sad.bagap.apiland.analysis.combination;
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
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__23=1, T__22=2, T__21=3, T__20=4, T__19=5, T__18=6, T__17=7, T__16=8, 
		T__15=9, T__14=10, T__13=11, T__12=12, T__11=13, T__10=14, T__9=15, T__8=16, 
		T__7=17, T__6=18, T__5=19, T__4=20, T__3=21, T__2=22, T__1=23, T__0=24, 
		NUMBER=25, NAME=26, NODATA_Value=27, WS=28;
	public static final String[] tokenNames = {
		"<INVALID>", "'/'", "'!='", "'>='", "'AND'", "'{'", "'log'", "'=='", "'<'", 
		"'elif'", "'}'", "'>'", "'if'", "'OR'", "'<='", "'!'", "'pow'", "'else'", 
		"'('", "')'", "'exp'", "'*'", "'+'", "','", "'-'", "NUMBER", "NAME", "'NODATA_Value'", 
		"WS"
	};
	public static final int
		RULE_evaluate = 0, RULE_combination = 1, RULE_combinationwithcoma = 2, 
		RULE_condition = 3, RULE_conditional = 4, RULE_genericboolterm = 5, RULE_andgenericboolterm = 6, 
		RULE_orgenericboolterm = 7, RULE_booltermwithcoma = 8, RULE_booltermnegation = 9, 
		RULE_boolterm = 10, RULE_leftoperation = 11, RULE_rightoperation = 12, 
		RULE_bloc = 13, RULE_operation = 14, RULE_term = 15, RULE_termminus = 16, 
		RULE_termwithcoma = 17, RULE_boolop = 18, RULE_name = 19, RULE_mathop = 20, 
		RULE_function = 21, RULE_function1param = 22, RULE_function2params = 23;
	public static final String[] ruleNames = {
		"evaluate", "combination", "combinationwithcoma", "condition", "conditional", 
		"genericboolterm", "andgenericboolterm", "orgenericboolterm", "booltermwithcoma", 
		"booltermnegation", "boolterm", "leftoperation", "rightoperation", "bloc", 
		"operation", "term", "termminus", "termwithcoma", "boolop", "name", "mathop", 
		"function", "function1param", "function2params"
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
			setState(48); combination();
			setState(49); match(EOF);
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
		public CombinationwithcomaContext combinationwithcoma() {
			return getRuleContext(CombinationwithcomaContext.class,0);
		}
		public BlocContext bloc() {
			return getRuleContext(BlocContext.class,0);
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
			setState(54);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(51); combinationwithcoma();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(52); condition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(53); bloc();
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

	public static class CombinationwithcomaContext extends ParserRuleContext {
		public CombinationContext combination() {
			return getRuleContext(CombinationContext.class,0);
		}
		public CombinationwithcomaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_combinationwithcoma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterCombinationwithcoma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitCombinationwithcoma(this);
		}
	}

	public final CombinationwithcomaContext combinationwithcoma() throws RecognitionException {
		CombinationwithcomaContext _localctx = new CombinationwithcomaContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_combinationwithcoma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56); match(T__6);
			setState(57); combination();
			setState(58); match(T__5);
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
		public ConditionalContext conditional(int i) {
			return getRuleContext(ConditionalContext.class,i);
		}
		public CombinationContext combination(int i) {
			return getRuleContext(CombinationContext.class,i);
		}
		public List<CombinationContext> combination() {
			return getRuleContexts(CombinationContext.class);
		}
		public List<ConditionalContext> conditional() {
			return getRuleContexts(ConditionalContext.class);
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
		enterRule(_localctx, 6, RULE_condition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60); match(T__12);
			setState(61); match(T__6);
			setState(62); conditional();
			setState(63); match(T__5);
			setState(64); match(T__19);
			setState(65); combination();
			setState(66); match(T__14);
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__15 || _la==T__12) {
				{
				{
				setState(67);
				_la = _input.LA(1);
				if ( !(_la==T__15 || _la==T__12) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(68); match(T__6);
				setState(69); conditional();
				setState(70); match(T__5);
				setState(71); match(T__19);
				setState(72); combination();
				setState(73); match(T__14);
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80); match(T__7);
			setState(81); match(T__19);
			setState(82); combination();
			setState(83); match(T__14);
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

	public static class ConditionalContext extends ParserRuleContext {
		public GenericbooltermContext genericboolterm() {
			return getRuleContext(GenericbooltermContext.class,0);
		}
		public ConditionalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditional; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterConditional(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitConditional(this);
		}
	}

	public final ConditionalContext conditional() throws RecognitionException {
		ConditionalContext _localctx = new ConditionalContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_conditional);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85); genericboolterm();
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

	public static class GenericbooltermContext extends ParserRuleContext {
		public AndgenericbooltermContext andgenericboolterm() {
			return getRuleContext(AndgenericbooltermContext.class,0);
		}
		public OrgenericbooltermContext orgenericboolterm() {
			return getRuleContext(OrgenericbooltermContext.class,0);
		}
		public BooltermContext boolterm() {
			return getRuleContext(BooltermContext.class,0);
		}
		public BooltermwithcomaContext booltermwithcoma() {
			return getRuleContext(BooltermwithcomaContext.class,0);
		}
		public BooltermnegationContext booltermnegation() {
			return getRuleContext(BooltermnegationContext.class,0);
		}
		public GenericbooltermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericboolterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterGenericboolterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitGenericboolterm(this);
		}
	}

	public final GenericbooltermContext genericboolterm() throws RecognitionException {
		GenericbooltermContext _localctx = new GenericbooltermContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_genericboolterm);
		try {
			setState(94);
			switch (_input.LA(1)) {
			case T__18:
			case T__9:
			case T__8:
			case T__6:
			case T__4:
			case T__0:
			case NUMBER:
			case NAME:
			case NODATA_Value:
				enterOuterAlt(_localctx, 1);
				{
				setState(90);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(87); boolterm();
					}
					break;
				case 2:
					{
					setState(88); booltermwithcoma();
					}
					break;
				case 3:
					{
					setState(89); booltermnegation();
					}
					break;
				}
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(92); andgenericboolterm();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(93); orgenericboolterm();
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

	public static class AndgenericbooltermContext extends ParserRuleContext {
		public List<GenericbooltermContext> genericboolterm() {
			return getRuleContexts(GenericbooltermContext.class);
		}
		public GenericbooltermContext genericboolterm(int i) {
			return getRuleContext(GenericbooltermContext.class,i);
		}
		public AndgenericbooltermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andgenericboolterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterAndgenericboolterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitAndgenericboolterm(this);
		}
	}

	public final AndgenericbooltermContext andgenericboolterm() throws RecognitionException {
		AndgenericbooltermContext _localctx = new AndgenericbooltermContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_andgenericboolterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96); match(T__20);
			setState(97); match(T__6);
			setState(98); genericboolterm();
			setState(99); match(T__1);
			setState(100); genericboolterm();
			setState(105);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(101); match(T__1);
				setState(102); genericboolterm();
				}
				}
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(108); match(T__5);
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

	public static class OrgenericbooltermContext extends ParserRuleContext {
		public List<GenericbooltermContext> genericboolterm() {
			return getRuleContexts(GenericbooltermContext.class);
		}
		public GenericbooltermContext genericboolterm(int i) {
			return getRuleContext(GenericbooltermContext.class,i);
		}
		public OrgenericbooltermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orgenericboolterm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterOrgenericboolterm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitOrgenericboolterm(this);
		}
	}

	public final OrgenericbooltermContext orgenericboolterm() throws RecognitionException {
		OrgenericbooltermContext _localctx = new OrgenericbooltermContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_orgenericboolterm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110); match(T__11);
			setState(111); match(T__6);
			setState(112); genericboolterm();
			setState(113); match(T__1);
			setState(114); genericboolterm();
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(115); match(T__1);
				setState(116); genericboolterm();
				}
				}
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(122); match(T__5);
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

	public static class BooltermwithcomaContext extends ParserRuleContext {
		public GenericbooltermContext genericboolterm() {
			return getRuleContext(GenericbooltermContext.class,0);
		}
		public BooltermwithcomaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booltermwithcoma; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterBooltermwithcoma(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitBooltermwithcoma(this);
		}
	}

	public final BooltermwithcomaContext booltermwithcoma() throws RecognitionException {
		BooltermwithcomaContext _localctx = new BooltermwithcomaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_booltermwithcoma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124); match(T__6);
			setState(125); genericboolterm();
			setState(126); match(T__5);
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

	public static class BooltermnegationContext extends ParserRuleContext {
		public GenericbooltermContext genericboolterm() {
			return getRuleContext(GenericbooltermContext.class,0);
		}
		public BooltermnegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booltermnegation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterBooltermnegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitBooltermnegation(this);
		}
	}

	public final BooltermnegationContext booltermnegation() throws RecognitionException {
		BooltermnegationContext _localctx = new BooltermnegationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_booltermnegation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); match(T__9);
			setState(129); genericboolterm();
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
		public BoolopContext boolop() {
			return getRuleContext(BoolopContext.class,0);
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
		enterRule(_localctx, 20, RULE_boolterm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131); leftoperation();
			setState(132); boolop();
			setState(133); rightoperation();
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
		enterRule(_localctx, 22, RULE_leftoperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135); operation();
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
		enterRule(_localctx, 24, RULE_rightoperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137); operation();
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

	public static class BlocContext extends ParserRuleContext {
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
		}
		public BlocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterBloc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitBloc(this);
		}
	}

	public final BlocContext bloc() throws RecognitionException {
		BlocContext _localctx = new BlocContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_bloc);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139); operation();
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
		enterRule(_localctx, 28, RULE_operation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141); term();
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__3) | (1L << T__2) | (1L << T__0))) != 0)) {
				{
				{
				setState(142); mathop();
				setState(143); term();
				}
				}
				setState(149);
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

	public static class TermContext extends ParserRuleContext {
		public TermminusContext termminus() {
			return getRuleContext(TermminusContext.class,0);
		}
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
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
		enterRule(_localctx, 30, RULE_term);
		try {
			setState(156);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(150); termwithcoma();
				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
				setState(151); termminus();
				}
				break;
			case NUMBER:
				enterOuterAlt(_localctx, 3);
				{
				setState(152); match(NUMBER);
				}
				break;
			case NAME:
				enterOuterAlt(_localctx, 4);
				{
				setState(153); name();
				}
				break;
			case T__18:
			case T__8:
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(154); function();
				}
				break;
			case NODATA_Value:
				enterOuterAlt(_localctx, 6);
				{
				setState(155); match(NODATA_Value);
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
		enterRule(_localctx, 32, RULE_termminus);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158); match(T__0);
			setState(159); term();
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
		public OperationContext operation() {
			return getRuleContext(OperationContext.class,0);
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
		enterRule(_localctx, 34, RULE_termwithcoma);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161); match(T__6);
			setState(162); operation();
			setState(163); match(T__5);
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
		enterRule(_localctx, 36, RULE_boolop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(165);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__22) | (1L << T__21) | (1L << T__17) | (1L << T__16) | (1L << T__13) | (1L << T__10))) != 0)) ) {
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

	public static class NameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(CombinationParser.NAME, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CombinationListener ) ((CombinationListener)listener).exitName(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167); match(NAME);
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
		enterRule(_localctx, 40, RULE_mathop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << T__3) | (1L << T__2) | (1L << T__0))) != 0)) ) {
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
		enterRule(_localctx, 42, RULE_function);
		try {
			setState(173);
			switch (_input.LA(1)) {
			case T__18:
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(171); function1param();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(172); function2params();
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
		enterRule(_localctx, 44, RULE_function1param);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			_la = _input.LA(1);
			if ( !(_la==T__18 || _la==T__4) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(176); match(T__6);
			setState(177); operation();
			setState(178); match(T__5);
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
		enterRule(_localctx, 46, RULE_function2params);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180); match(T__8);
			setState(181); match(T__6);
			setState(182); operation();
			setState(183); match(T__1);
			setState(184); match(NUMBER);
			setState(185); match(T__5);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\36\u00be\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\5\39\n\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5N\n\5\f\5\16\5Q\13\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\7\5\7]\n\7\3\7\3\7\5\7a\n\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\7\bj\n\b\f\b\16\bm\13\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\7\tx\n\t\f\t\16\t{\13\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\7\20"+
		"\u0094\n\20\f\20\16\20\u0097\13\20\3\21\3\21\3\21\3\21\3\21\3\21\5\21"+
		"\u009f\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\25\3\25\3\26"+
		"\3\26\3\27\3\27\5\27\u00b0\n\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36"+
		" \"$&(*,.\60\2\6\4\2\13\13\16\16\6\2\4\5\t\n\r\r\20\20\5\2\3\3\27\30\32"+
		"\32\4\2\b\b\26\26\u00b5\2\62\3\2\2\2\48\3\2\2\2\6:\3\2\2\2\b>\3\2\2\2"+
		"\nW\3\2\2\2\f`\3\2\2\2\16b\3\2\2\2\20p\3\2\2\2\22~\3\2\2\2\24\u0082\3"+
		"\2\2\2\26\u0085\3\2\2\2\30\u0089\3\2\2\2\32\u008b\3\2\2\2\34\u008d\3\2"+
		"\2\2\36\u008f\3\2\2\2 \u009e\3\2\2\2\"\u00a0\3\2\2\2$\u00a3\3\2\2\2&\u00a7"+
		"\3\2\2\2(\u00a9\3\2\2\2*\u00ab\3\2\2\2,\u00af\3\2\2\2.\u00b1\3\2\2\2\60"+
		"\u00b6\3\2\2\2\62\63\5\4\3\2\63\64\7\2\2\3\64\3\3\2\2\2\659\5\6\4\2\66"+
		"9\5\b\5\2\679\5\34\17\28\65\3\2\2\28\66\3\2\2\28\67\3\2\2\29\5\3\2\2\2"+
		":;\7\24\2\2;<\5\4\3\2<=\7\25\2\2=\7\3\2\2\2>?\7\16\2\2?@\7\24\2\2@A\5"+
		"\n\6\2AB\7\25\2\2BC\7\7\2\2CD\5\4\3\2DO\7\f\2\2EF\t\2\2\2FG\7\24\2\2G"+
		"H\5\n\6\2HI\7\25\2\2IJ\7\7\2\2JK\5\4\3\2KL\7\f\2\2LN\3\2\2\2ME\3\2\2\2"+
		"NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3\2\2\2QO\3\2\2\2RS\7\23\2\2ST\7\7\2"+
		"\2TU\5\4\3\2UV\7\f\2\2V\t\3\2\2\2WX\5\f\7\2X\13\3\2\2\2Y]\5\26\f\2Z]\5"+
		"\22\n\2[]\5\24\13\2\\Y\3\2\2\2\\Z\3\2\2\2\\[\3\2\2\2]a\3\2\2\2^a\5\16"+
		"\b\2_a\5\20\t\2`\\\3\2\2\2`^\3\2\2\2`_\3\2\2\2a\r\3\2\2\2bc\7\6\2\2cd"+
		"\7\24\2\2de\5\f\7\2ef\7\31\2\2fk\5\f\7\2gh\7\31\2\2hj\5\f\7\2ig\3\2\2"+
		"\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2ln\3\2\2\2mk\3\2\2\2no\7\25\2\2o\17\3"+
		"\2\2\2pq\7\17\2\2qr\7\24\2\2rs\5\f\7\2st\7\31\2\2ty\5\f\7\2uv\7\31\2\2"+
		"vx\5\f\7\2wu\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z|\3\2\2\2{y\3\2\2\2"+
		"|}\7\25\2\2}\21\3\2\2\2~\177\7\24\2\2\177\u0080\5\f\7\2\u0080\u0081\7"+
		"\25\2\2\u0081\23\3\2\2\2\u0082\u0083\7\21\2\2\u0083\u0084\5\f\7\2\u0084"+
		"\25\3\2\2\2\u0085\u0086\5\30\r\2\u0086\u0087\5&\24\2\u0087\u0088\5\32"+
		"\16\2\u0088\27\3\2\2\2\u0089\u008a\5\36\20\2\u008a\31\3\2\2\2\u008b\u008c"+
		"\5\36\20\2\u008c\33\3\2\2\2\u008d\u008e\5\36\20\2\u008e\35\3\2\2\2\u008f"+
		"\u0095\5 \21\2\u0090\u0091\5*\26\2\u0091\u0092\5 \21\2\u0092\u0094\3\2"+
		"\2\2\u0093\u0090\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095"+
		"\u0096\3\2\2\2\u0096\37\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u009f\5$\23"+
		"\2\u0099\u009f\5\"\22\2\u009a\u009f\7\33\2\2\u009b\u009f\5(\25\2\u009c"+
		"\u009f\5,\27\2\u009d\u009f\7\35\2\2\u009e\u0098\3\2\2\2\u009e\u0099\3"+
		"\2\2\2\u009e\u009a\3\2\2\2\u009e\u009b\3\2\2\2\u009e\u009c\3\2\2\2\u009e"+
		"\u009d\3\2\2\2\u009f!\3\2\2\2\u00a0\u00a1\7\32\2\2\u00a1\u00a2\5 \21\2"+
		"\u00a2#\3\2\2\2\u00a3\u00a4\7\24\2\2\u00a4\u00a5\5\36\20\2\u00a5\u00a6"+
		"\7\25\2\2\u00a6%\3\2\2\2\u00a7\u00a8\t\3\2\2\u00a8\'\3\2\2\2\u00a9\u00aa"+
		"\7\34\2\2\u00aa)\3\2\2\2\u00ab\u00ac\t\4\2\2\u00ac+\3\2\2\2\u00ad\u00b0"+
		"\5.\30\2\u00ae\u00b0\5\60\31\2\u00af\u00ad\3\2\2\2\u00af\u00ae\3\2\2\2"+
		"\u00b0-\3\2\2\2\u00b1\u00b2\t\5\2\2\u00b2\u00b3\7\24\2\2\u00b3\u00b4\5"+
		"\36\20\2\u00b4\u00b5\7\25\2\2\u00b5/\3\2\2\2\u00b6\u00b7\7\22\2\2\u00b7"+
		"\u00b8\7\24\2\2\u00b8\u00b9\5\36\20\2\u00b9\u00ba\7\31\2\2\u00ba\u00bb"+
		"\7\33\2\2\u00bb\u00bc\7\25\2\2\u00bc\61\3\2\2\2\138O\\`ky\u0095\u009e"+
		"\u00af";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}