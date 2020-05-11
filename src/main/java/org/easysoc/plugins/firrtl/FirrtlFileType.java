package org.easysoc.plugins.firrtl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.Icons;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FirrtlFileType extends LanguageFileType {
  public static final FirrtlFileType INSTANCE = new FirrtlFileType();

  protected FirrtlFileType() {
    super(FirrtlLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Firrtl";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Firrtl language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "fir";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return Icons.FirrtlFileType;
  }
}
