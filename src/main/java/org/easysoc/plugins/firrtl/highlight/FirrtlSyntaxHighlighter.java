package org.easysoc.plugins.firrtl.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.easysoc.plugins.firrtl.FirrtlLanguage;
import org.easysoc.plugins.firrtl.parser.FirrtlLanguageLexer;
import org.easysoc.plugins.firrtl.parser.FirrtlLanguageParser;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/** A highlighter is really just a mapping from token type to
 *  some text attributes using {@link #getTokenHighlights(IElementType)}.
 *  The reason that it returns an array, TextAttributesKey[], is
 *  that you might want to mix the attributes of a few known highlighters.
 *  A {@link TextAttributesKey} is just a name for that a theme
 *  or IDE skin can set. For example, {@link DefaultLanguageHighlighterColors#KEYWORD}
 *  is the key that maps to what identifiers look like in the editor.
 *  To change it, see dialog: Editor > Colors & Fonts > Language Defaults.
 *
 *  From <a href="http://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html">doc</a>:
 *  "The mapping of the TextAttributesKey to specific attributes used
 *  in an editor is defined by the EditorColorsScheme class, and can
 *  be configured by the user if the plugin provides an appropriate
 *  configuration interface.
 *  ...
 *  The syntax highlighter returns the {@link TextAttributesKey}
 * instances for each token type which needs special highlighting.
 * For highlighting lexer errors, the standard TextAttributesKey
 * for bad characters HighlighterColors.BAD_CHARACTER can be used."
 */
public class FirrtlSyntaxHighlighter extends SyntaxHighlighterBase {
	private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

	public static final TextAttributesKey ID =
					createTextAttributesKey("FIRRTL_ID", DefaultLanguageHighlighterColors.IDENTIFIER);
	public static final TextAttributesKey KEYWORD =
					createTextAttributesKey("FIRRTL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	public static final TextAttributesKey STRING =
					createTextAttributesKey("FIRRTL_STRING", DefaultLanguageHighlighterColors.STRING);
	public static final TextAttributesKey LINE_COMMENT =
					createTextAttributesKey("FIRRTL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	public static final TextAttributesKey BLOCK_COMMENT =
					createTextAttributesKey("FIRRTL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
	public static final TextAttributesKey FUNCTION_CALL =
					createTextAttributesKey("FIRRTL_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL);

	// maps all tokens and rule names into IElementType objects: {@link TokenIElementType} and {@link RuleIElementType}
	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(FirrtlLanguage.INSTANCE,
						FirrtlLanguageParser.tokenNames,
						FirrtlLanguageParser.ruleNames);
	}

	@NotNull
	@Override
	public Lexer getHighlightingLexer() {
		FirrtlLanguageLexer lexer = new FirrtlLanguageLexer(null);
		// syntax highlight doesn't need to consider indentation
		lexer.setIgnoreIndent(true);
		return new ANTLRLexerAdaptor(FirrtlLanguage.INSTANCE, lexer);
	}

	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
//		List<TokenIElementType> list = PSIElementTypeFactory.getTokenIElementTypes(SampleLanguage.INSTANCE);
//		if ( !(tokenType instanceof TokenIElementType) ) return EMPTY_KEYS;
		TokenIElementType myType = (TokenIElementType)tokenType;
		int tokentype = myType.getANTLRTokenType();
		TextAttributesKey attrKey;
		// compare with IntStream
		switch ( tokentype ) {
			case FirrtlLanguageLexer.Id :
				attrKey = ID;
				break;
			case FirrtlLanguageLexer.Key_circuit :
			case FirrtlLanguageLexer.Key_module :
			case FirrtlLanguageLexer.Key_extmodule :
			case FirrtlLanguageLexer.Key_parameter :
			case FirrtlLanguageLexer.Key_input :
			case FirrtlLanguageLexer.Key_output :
			case FirrtlLanguageLexer.Key_UInt :
			case FirrtlLanguageLexer.Key_SInt :
			case FirrtlLanguageLexer.Key_Clock :
			case FirrtlLanguageLexer.Key_Analog :
			case FirrtlLanguageLexer.Key_Fixed :
			case FirrtlLanguageLexer.Key_flip :
			case FirrtlLanguageLexer.Key_wire :
			case FirrtlLanguageLexer.Key_reg :
			case FirrtlLanguageLexer.Key_with :
			case FirrtlLanguageLexer.Key_reset :
			case FirrtlLanguageLexer.Key_mem :
			case FirrtlLanguageLexer.Key_depth :
			case FirrtlLanguageLexer.Key_reader :
			case FirrtlLanguageLexer.Key_writer :
			case FirrtlLanguageLexer.Key_readwriter :
			case FirrtlLanguageLexer.Key_inst :
			case FirrtlLanguageLexer.Key_of :
			case FirrtlLanguageLexer.Key_node :
			case FirrtlLanguageLexer.Key_is :
			case FirrtlLanguageLexer.Key_invalid :
			case FirrtlLanguageLexer.Key_when :
			case FirrtlLanguageLexer.Key_else :
			case FirrtlLanguageLexer.Key_stop :
			case FirrtlLanguageLexer.Key_printf :
			case FirrtlLanguageLexer.Key_skip :
			case FirrtlLanguageLexer.Key_old :
			case FirrtlLanguageLexer.Key_new :
			case FirrtlLanguageLexer.Key_undefined :
			case FirrtlLanguageLexer.Key_mux :
			case FirrtlLanguageLexer.Key_validif :
			case FirrtlLanguageLexer.Key_cmem :
			case FirrtlLanguageLexer.Key_smem :
			case FirrtlLanguageLexer.Key_mport :
			case FirrtlLanguageLexer.Key_infer :
			case FirrtlLanguageLexer.Key_read :
			case FirrtlLanguageLexer.Key_write :
			case FirrtlLanguageLexer.Key_rdwr :
				attrKey = KEYWORD;
				break;
			case FirrtlLanguageLexer.StringLit :
			case FirrtlLanguageLexer.RawString :
				attrKey = STRING;
				break;
			case FirrtlLanguageLexer.FileInfo :
			case FirrtlLanguageLexer.COMMENT :
				attrKey = LINE_COMMENT;
				break;
			default :
				return EMPTY_KEYS;
		}
		return new TextAttributesKey[] {attrKey};
	}
}
