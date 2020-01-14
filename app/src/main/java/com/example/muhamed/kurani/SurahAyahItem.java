package com.example.muhamed.kurani;

public class SurahAyahItem {
    private String SurahNumber;
    private String AyahNumber;

    public SurahAyahItem(String surahItem,String ayahItem)
    {
        SurahNumber = surahItem;
        AyahNumber = ayahItem;
    }
    public String getSurahNumber() {
        return SurahNumber;
    }

    public String getAyahNumber() {
        return AyahNumber;
    }
}
