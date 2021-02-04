package org.easysoc.plugins.firrtl.psi.leaf;

import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;


public class DedentLeafNode extends LeafPsiElement {
	public DedentLeafNode(IElementType type, CharSequence text) {
		super(type, text);
	}
}
