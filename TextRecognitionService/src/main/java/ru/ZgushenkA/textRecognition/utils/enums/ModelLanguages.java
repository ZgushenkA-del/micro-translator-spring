package ru.ZgushenkA.textRecognition.utils.enums;

public enum ModelLanguages {
    OSD("Автораспознование","osd"),
    RUS("Русский", "rus"),
    ENG("Английский", "eng"),
    CHI_SIM("Китайский упрощенный", "chi_sim"),
    CHI_TRA("Китайский традиционный", "chi_tra"),
    DEU("Немецкий","deu");
    private final String value;
    private final String ref;
    ModelLanguages(String value, String ref) {
        this.value = value;
        this.ref = ref;
    }

    public String getValue() {
        return value;
    }

    public String getRef() {
        return ref;
    }
}
