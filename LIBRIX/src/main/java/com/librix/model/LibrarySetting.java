package com.librix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "library_settings")
public class LibrarySetting {
    @Id @Column(length = 100) private String settingKey;
    @Column(nullable = false, length = 1000) private String settingValue;
    protected LibrarySetting() { }
    public LibrarySetting(String settingKey, String settingValue) { this.settingKey = settingKey; this.settingValue = settingValue; }
    public String getSettingKey() { return settingKey; } public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }
}
