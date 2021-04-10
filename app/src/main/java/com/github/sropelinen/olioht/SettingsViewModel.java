package com.github.sropelinen.olioht;

import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    private boolean darkMode;

    public SettingsViewModel() {
        darkMode = false;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isDarkMode() {
        return darkMode;
    }
}
