package org.easysoc.plugins.firrtl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.easysoc.plugins.firrtl.parser.FirrtlLanguageLexer;
import org.easysoc.plugins.firrtl.parser.FirrtlLanguageParser;
import org.easysoc.plugins.firrtl.psi.*;
import org.easysoc.plugins.firrtl.psi.subtree.*;
import org.jetbrains.annotations.NotNull;

public class FirrtlParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE = new IFileElementType(FirrtlLanguage.INSTANCE);

//	public static TokenIElementType ID;

	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(FirrtlLanguage.INSTANCE,
		                                                  FirrtlLanguageParser.tokenNames,
		                                                  FirrtlLanguageParser.ruleNames);
//		List<TokenIElementType> tokenIElementTypes =
//			PSIElementTypeFactory.getTokenIElementTypes(FirrtlLanguage.INSTANCE);
//		ID = tokenIElementTypes.get(FirrtlLanguageLexer.Id);
	}

	public static final TokenSet COMMENTS =
		PSIElementTypeFactory.createTokenSet(
			FirrtlLanguage.INSTANCE,
			FirrtlLanguageLexer.COMMENT);

	public static final TokenSet WHITESPACE =
		PSIElementTypeFactory.createTokenSet(
			FirrtlLanguage.INSTANCE,
			FirrtlLanguageLexer.WHITESPACE);

	public static final TokenSet STRING =
		PSIElementTypeFactory.createTokenSet(
			FirrtlLanguage.INSTANCE,
			FirrtlLanguageLexer.StringLit);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		FirrtlLanguageLexer lexer = new FirrtlLanguageLexer(null);
		return new ANTLRLexerAdaptor(FirrtlLanguage.INSTANCE, lexer);
	}

	@NotNull
	public PsiParser createParser(final Project project) {
		final FirrtlLanguageParser myparser = new FirrtlLanguageParser(null);
		return new ANTLRParserAdaptor(FirrtlLanguage.INSTANCE, myparser) {
			@Override
			protected ParseTree parse(Parser parser, IElementType root) {
				return ((FirrtlLanguageParser) parser).circuit();
			}
		};
	}

	/** "Tokens of those types are automatically skipped by PsiBuilder." */
	@NotNull
	public TokenSet getWhitespaceTokens() {
		return WHITESPACE;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return STRING;
	}

	/** What is the IFileElementType of the root parse tree node? It
	 *  is called from {@link #createFile(FileViewProvider)} at least.
	 */
	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	/** Create the root of your PSI tree (a PsiFile).
	 *
	 *  From IntelliJ IDEA Architectural Overview:
	 *  "A PSI (Program Structure Interface) file is the root of a structure
	 *  representing the contents of a file as a hierarchy of elements
	 *  in a particular programming language."
	 *
	 *  PsiFile is to be distinguished from a FileASTNode, which is a parse
	 *  tree node that eventually becomes a PsiFile. From PsiFile, we can get
	 *  it back via: {@link PsiFile#getNode}.
	 */
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new FirrtlFile(viewProvider);
	}

	/** Convert from *NON-LEAF* parse node (AST they call it)
	 *  to PSI node. Leaves are created in the AST factory.
	 *  Rename re-factoring can cause this to be
	 *  called on a TokenIElementType since we want to rename ID nodes.
	 *  In that case, this method is called to create the root node
	 *  but with ID type. Kind of strange, but we can simply create a
	 *  ASTWrapperPsiElement to make everything work correctly.
	 *
	 *  RuleIElementType.  Ah! It's that ID is the root
	 *  IElementType requested to parse, which means that the root
	 *  node returned from parsetree->PSI conversion.  But, it
	 *  must be a CompositeElement! The adaptor calls
	 *  rootMarker.done(root) to finish off the PSI conversion.
	 *  See {@link ANTLRParserAdaptor#parse(IElementType root,
	 *  PsiBuilder)}
	 *
	 *  If you don't care to distinguish PSI nodes by type, it is
	 *  sufficient to create a {@link ANTLRPsiNode} around
	 *  the parse tree node
	 */
	@NotNull
	public PsiElement createElement(ASTNode node) {
		IElementType elType = node.getElementType();
		// no need to check
//		if ( !(elType instanceof RuleIElementType) ) {
//			return new ANTLRPsiNode(node);
//		}
		RuleIElementType ruleElType = (RuleIElementType) elType;
		switch ( ruleElType.getRuleIndex() ) {
			case FirrtlLanguageParser.RULE_circuit :
				return new CircuitTreeNode(node);
			case FirrtlLanguageParser.RULE_module :
				return new ModuleTreeNode(node);
			case FirrtlLanguageParser.RULE_moduleBlock :
				return new ModuleBlockTreeNode(node);
			case FirrtlLanguageParser.RULE_port :
				return new PortTreeNode(node);
			case FirrtlLanguageParser.RULE_suite :
				return new SuiteTreeNode(node);
			case FirrtlLanguageParser.RULE_id :
				return new IdentifierTreeNode(node);
			case FirrtlLanguageParser.RULE_mem :
				return new MemTreeNode(node);
			default :
				return new ANTLRPsiNode(node);
		}
	}
}
