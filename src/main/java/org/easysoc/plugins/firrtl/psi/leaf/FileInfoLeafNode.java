package org.easysoc.plugins.firrtl.psi.leaf;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.easysoc.plugins.firrtl.resolve.reference.FileInfoRef;


public class FileInfoLeafNode extends LeafPsiElement {
	public FileInfoLeafNode(IElementType type, CharSequence text) {
		super(type, text);
	}

	@Override
	public String getName() {
		return getText();
	}

	@Override
	public PsiReference getReference() {
		TextRange textRange = new TextRange(2, getText().length() - 1);
		return new FileInfoRef(this, textRange);
	}
}
