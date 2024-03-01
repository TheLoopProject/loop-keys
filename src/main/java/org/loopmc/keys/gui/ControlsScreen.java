package org.loopmc.keys.gui;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.locale.LanguageManager;
import org.loopmc.keys.ResettingKeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;

public class ControlsScreen extends Screen {
    private final Screen parentScreen;
    private final GameOptions gameOptions;
    private String title;
    private KeysListWidget keysListWidget;
    private final ArrayList<ButtonWidget> keyButtons = new ArrayList<>();
    private final LinkedList<ResetButtonWidget> resetButtons = new LinkedList<>();
    private int selectedKey = -1;

    public ControlsScreen(Screen screen, GameOptions gameOptions) {
        this.parentScreen = screen;
        this.gameOptions = gameOptions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        LanguageManager languageManager = LanguageManager.getInstance();

        this.title = languageManager.translate("controls.title");

        this.keysListWidget = new ControlsScreen.KeysListWidget();

        this.buttons.add(new ButtonWidget(100, this.width / 2 - 100, this.height - 28, languageManager.translate("gui.done")));

        for (int i = 0; i < this.gameOptions.keyBindings.length; i++) {
            ButtonWidget key = new ButtonWidget(200 + i, 0, 0, 75, 20, this.gameOptions.getOptionName(i));
            this.keyButtons.add(key);
            this.resetButtons.add(new ResetButtonWidget(0, 0, 50, 20, gameOptions.keyBindings[i]));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.selectedKey >= 0) {
            this.gameOptions.setKeyBinding(this.selectedKey, -100 + mouseButton);

            this.keyButtons.get(this.selectedKey).message = this.gameOptions.getOptionName(this.selectedKey);

            this.selectedKey = -1;

            KeyBinding.resetMapping();

            return;
        }

        if (mouseButton == 0) {
			for (int i = 0, size = keyButtons.size(); i < size; i++) {
				ButtonWidget btn = keyButtons.get(i);
				if (btn.isMouseOver(this.minecraft, mouseX, mouseY)) {
					this.minecraft.soundSystem.play("random.click", 1.0F, 1.0F);

					this.buttonClicked(btn);
                    break;
				} else {
                    ResetButtonWidget reset = resetButtons.get(i);
                    if (reset.isMouseOver(minecraft, mouseX, mouseY)){
                        this.minecraft.soundSystem.play("random.click", 1.0F, 1.0F);

                        reset.reset();
                        btn.message = this.gameOptions.getOptionName(btn.id - 200);

                        if (selectedKey != -1){
                            ButtonWidget selected = keyButtons.get(selectedKey);
                            selected.message = this.gameOptions.getOptionName(selected.id - 200);
                        }
                        selectedKey = -1;
                        break;
                    }
                }
			}
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyPressed(char chr, int key) {
        if (this.selectedKey >= 0) {

            if (key == Keyboard.KEY_ESCAPE){
                this.gameOptions.setKeyBinding(this.selectedKey, Keyboard.KEY_NONE);
            } else {
                this.gameOptions.setKeyBinding(this.selectedKey, key);
            }

            this.keyButtons.get(this.selectedKey).message = this.gameOptions.getOptionName(this.selectedKey);

            this.selectedKey = -1;

            KeyBinding.resetMapping();
        } else {
            super.keyPressed(chr, key);
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget btn) {

        if (btn.active) {
            if (btn.id == 100) {
                this.minecraft.openScreen(this.parentScreen);
            } else {
                this.selectedKey = btn.id - 200;

                btn.message = "> " + this.gameOptions.getOptionName(btn.id - 200) + " <";
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float tickDelta) {
        this.renderBackground();

        this.keysListWidget.render(mouseX, mouseY, tickDelta);

        this.drawCenteredString(this.textRenderer, this.title, this.width / 2, 20, 16777215);

        ((ButtonWidget) this.buttons.get(0)).render(this.minecraft, mouseX, mouseY);
    }

    class KeysListWidget extends ListWidget {
        public KeysListWidget() {
            super(ControlsScreen.this.minecraft, ControlsScreen.this.width, ControlsScreen.this.height, 32, ControlsScreen.this.height - 32, 24);
        }

        @Override
        protected int size() {
            return ControlsScreen.this.gameOptions.keyBindings.length;
        }

        @Override
        protected int getHeight() {
            return ControlsScreen.this.gameOptions.keyBindings.length * 24;
        }

        @Override
        protected void entryClicked(int i, boolean bl) {

        }

        @Override
        protected int getScrollbarPosition() {
            return super.getScrollbarPosition()+12;
        }

        @Override
        protected boolean isEntrySelected(int index) {
            return false;
        }

        @Override
        protected void renderBackground() {
            ControlsScreen.this.renderBackground();
        }

        @Override
        protected void renderEntry(int i, int j, int k, int l, BufferBuilder tesselator) {
            final KeyBinding keyBinding = ControlsScreen.this.gameOptions.keyBindings[i];

            LanguageManager languageManager = LanguageManager.getInstance();

            ControlsScreen.this.drawString(ControlsScreen.this.textRenderer, languageManager.translate(keyBinding.name), j, k + (textRenderer.fontHeight - 2), 16777215);

            ButtonWidget btn = ControlsScreen.this.keyButtons.get(i);

            btn.x = j + 117;
            btn.y = k;

            btn.render(ControlsScreen.this.minecraft, mouseX, mouseY);

            ButtonWidget reset = ControlsScreen.this.resetButtons.get(i);

            reset.x = btn.x + 75;
            reset.y = k;

            reset.render(minecraft, mouseX, mouseY);
        }

        @Override
        public void buttonClicked(ButtonWidget btn) {
            System.out.println(btn.id);
        }
    }

    static class ResetButtonWidget extends ButtonWidget {

        private final KeyBinding binding;
        public ResetButtonWidget(int x, int y, int width, int height, KeyBinding key) {
            super(9999, x, y, width, height, I18n.translate("gui.reset"));
            this.binding = key;
        }

        public void reset(){
            ((ResettingKeyBinding)binding).reset();
        }

    }
}
