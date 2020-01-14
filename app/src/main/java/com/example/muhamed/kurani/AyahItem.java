package com.example.muhamed.kurani;

public class AyahItem {
    private String NumberOfAyah;
    private String Ayah_Arab;
    private String Ayah_Alb;

    public AyahItem(String numberOfAyah,String ayah_Arab,String ayah_Alb)
    {
        NumberOfAyah = numberOfAyah;
        Ayah_Arab = ayah_Arab;
        Ayah_Alb = ayah_Alb;
    }

    public String getNumberOfAyah() {
        return NumberOfAyah;
    }

    public String getAyah_Arab() {
        return Ayah_Arab;
    }

    public String getAyah_Alb() {
        return Ayah_Alb;
    }
}
