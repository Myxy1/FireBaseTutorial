package com.example.firebasetutorial;

public class File {

    private String fileNev;
    private String fileImageUrl;

    public File()
    {

    }

    public File(String nev, String URL)
    {
        if (nev.trim().equals(""))
        {
            nev = "Nincs nev";
        }
        fileNev = nev;
        fileImageUrl = URL;
    }

    public String getFileNev() {
        return fileNev;
    }

    public String getFileImageUrl() {
        return fileImageUrl;
    }
}
