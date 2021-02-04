package org.easysoc.plugins.firrtl.psi.leaf;

import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;


public class IdentifierLeafNode extends LeafPsiElement /*implements PsiNamedElement*/ {
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
