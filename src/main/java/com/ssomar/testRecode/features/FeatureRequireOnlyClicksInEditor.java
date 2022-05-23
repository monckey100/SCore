package com.ssomar.testRecode.features;

import com.ssomar.testRecode.editor.NewGUIManager;
import com.ssomar.testRecode.editor.NewInteractionClickedGUIManager;
import org.bukkit.entity.Player;

public interface FeatureRequireOnlyClicksInEditor {

    void clickParentEditor(Player editor, NewGUIManager manager);

    boolean noShiftclicked(Player editor, NewGUIManager manager);

    boolean noShiftLeftclicked(Player editor, NewGUIManager manager);

    boolean noShiftRightclicked(Player editor, NewGUIManager manager);

    boolean shiftClicked(Player editor, NewGUIManager manager);

    boolean shiftLeftClicked(Player editor, NewGUIManager manager);

    boolean shiftRightClicked(Player editor, NewGUIManager manager);

    boolean leftClicked(Player editor, NewGUIManager manager);

    boolean rightClicked(Player editor, NewGUIManager manager);
}
