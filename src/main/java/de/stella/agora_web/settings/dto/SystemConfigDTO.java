package de.stella.agora_web.settings.dto;

/**
 * 🔧 DTO para configuraciones del sistema DTO compartido para evitar
 * duplicación
 */
public class SystemConfigDTO {

    private String[] availableThemes;
    private String[] availableLanguages;
    private String[] accessibilityOptions;
    private String defaultTheme;
    private String defaultLanguage;

    // Constructors
    public SystemConfigDTO() {
    }

    public SystemConfigDTO(String[] availableThemes, String[] availableLanguages,
            String[] accessibilityOptions, String defaultTheme, String defaultLanguage) {
        this.availableThemes = availableThemes;
        this.availableLanguages = availableLanguages;
        this.accessibilityOptions = accessibilityOptions;
        this.defaultTheme = defaultTheme;
        this.defaultLanguage = defaultLanguage;
    }

    // Getters and Setters
    public String[] getAvailableThemes() {
        return availableThemes;
    }

    public void setAvailableThemes(String[] availableThemes) {
        this.availableThemes = availableThemes;
    }

    public String[] getAvailableLanguages() {
        return availableLanguages;
    }

    public void setAvailableLanguages(String[] availableLanguages) {
        this.availableLanguages = availableLanguages;
    }

    public String[] getAccessibilityOptions() {
        return accessibilityOptions;
    }

    public void setAccessibilityOptions(String[] accessibilityOptions) {
        this.accessibilityOptions = accessibilityOptions;
    }

    public String getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(String defaultTheme) {
        this.defaultTheme = defaultTheme;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
