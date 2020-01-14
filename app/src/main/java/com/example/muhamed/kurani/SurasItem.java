package com.example.muhamed.kurani;

public class SurasItem {

    private int NumberOfSurah;
    private String SurahName;
    private String NrOfAyasLabel;
    private int NrOfAyas;

    public SurasItem(int numberOfSurah,String surahName,String nrOfAyasLabel,int nrOfAyas)
    {
        NumberOfSurah = numberOfSurah;
        SurahName = surahName;
        NrOfAyasLabel = nrOfAyasLabel;
        NrOfAyas = nrOfAyas;
    }

    public int getNumberOfSurah() {
        return NumberOfSurah;
    }

    public String getSurahName() {
        return SurahName;
    }

    public String getNrOfAyasLabel() {
        return NrOfAyasLabel;
    }

    public int getNrOfAyas() {
        return NrOfAyas;
    }
}
