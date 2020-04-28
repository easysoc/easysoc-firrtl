package org.easysoc.plugins.firrtl;

import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.easysoc.plugins.firrtl.parser.FirrtlLanguageLexer;
import org.easysoc.plugins.firrtl.psi.leaf.*;
import org.jetbrains.annotations.NotNull;

/**
 * How to create parse tree nodes (Jetbrains calls them AST nodes). Later
 * non-leaf nodes are converted to PSI nodes by the {@link ParserDefinition}.
 * Leaf nodes are already considered PSI nodes.  This is mostly just
 * {@link DefaultASTFactoryImpl} but with comments on the methods that you might want
 * to override.
 */
public class FirrtlASTFactory extends DefaultASTFactoryImpl {

  /**
   * Create a parse tree (AST) leaf node from a token. Doubles as a PSI leaf node.
   * Does not see whitespace tokens.  Default impl makes {@link LeafPsiElement}
   * or {@link PsiCommentImpl} depending on {@link ParserDefinition#getCommentTokens()}.
   */
  @NotNull
  @Override
  public LeafElement createLeaf(@NotNull IElementType type, CharSequence text) {
    int tokenType = ((TokenIElementType) type).getANTLRTokenType();
    switch (tokenType) {
      case FirrtlLanguageLexer.Id:
        return new IdentifierLeafNode(type, text);
      case FirrtlLanguageLexer.FileInfo:
        return new FileInfoLeafNode(type, text);
      default:
        return super.createLeaf(type, text);
    }
  }
}
