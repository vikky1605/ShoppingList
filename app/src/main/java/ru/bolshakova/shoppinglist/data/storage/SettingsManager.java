package ru.bolshakova.shoppinglist.data.storage;

public class SettingsManager {

        private static SettingsManager INSTANCE = null;
        private SettingStorage mSettingsStorage;

        public SettingsManager() {
            this.mSettingsStorage = new SettingStorage();
        }

        public static SettingsManager getINSTANCE() {
            if (INSTANCE == null) {
                INSTANCE = new SettingsManager();
            }
            return INSTANCE;
        }

        public SettingStorage getSettingsStorage() {
            return mSettingsStorage;
        }
    }


