package org.loopmc.keys.gui;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.locale.LanguageManager;

import java.util.ArrayList;

public class ControlsScreen extends Screen {
    private final Screen parentScreen;
    private final GameOptions gameOptions;
    private String title;
    private KeysListWidget keysListWidget;
    private final ArrayList<ButtonWidget> keyButtons = new ArrayList<>();
    private int selectedKey = -1;

    public ControlsScreen(Screen screen, GameOptions gameOptions) {
        this.parentScreen = screen;
        this.gameOptions = gameOptions;
    }

    @Override
    public void init() {
        LanguageManager languageManager = LanguageManager.getInstance();

        this.title = languageManager.translate("controls.title");

        this.keysListWidget = new ControlsScreen.KeysListWidget();

        this.buttons.add(new ButtonWidget(100, this.width / 2 - 100, this.height - 28, languageManager.translate("gui.done")));

        for (int i = 0; i < this.gameOptions.keyBindings.length; i++) {
            this.keyButtons.add(new ButtonWidget(200 + i, 0, 0, 100, 20, this.gameOptions.getOptionName(i)));
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
            for (ButtonWidget btn : this.keyButtons) {
                if (btn.isMouseOver(this.minecraft, mouseX, mouseY)) {
                    this.minecraft.soundSystem.play("random.click", 1.0F, 1.0F);

                    this.buttonClicked(btn);
                }
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyPressed(char chr, int key) {
        if (this.selectedKey >= 0) {
            this.gameOptions.setKeyBinding(this.selectedKey, key);

            this.keyButtons.get(this.selectedKey).message = this.gameOptions.getOptionName(this.selectedKey);

            this.selectedKey = -1;

            KeyBinding.resetMapping();
        } else {
            super.keyPressed(chr, key);
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget btn) {
        System.out.println(btn.id);

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
        }

        @Override
        public void buttonClicked(ButtonWidget btn) {
            System.out.println(btn.id);
        }
    }
}
