package org.easysoc.plugins.firrtl.psi.leaf;

import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;


public class IdentifierLeafNode extends ANTLRPsiLeafNode /*implements PsiNamedElement*/ {
	public IdentifierLeafNode(IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public String getName() {
		return getText();
	}

//	@Override
//	public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
//		return this;
//	}

//	@Override
//	public PsiReference getReference() {
//		return null;
//	}
}
