package com.example.muhamed.kurani;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.util.Output;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public final class Helper {
    public static ArrayList<SurasItem> ReadSurasListXmlData(Context context) {

        InputSource is = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            is = new InputSource(context.getResources().getAssets().open("Albanian/ChapterNamesAlbanian.xml"));
            db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(is.getByteStream()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("Chapter");

        ArrayList<SurasItem> quranSuras = new ArrayList<SurasItem>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element elmnt = (Element) node;

            int surahNumber = Integer.parseInt(elmnt.getAttribute("ChapterID"));
            String surahName = elmnt.getAttribute("ChapterName");
            int numberOfAyas = getNrOfAyasForSurah(surahNumber, context);
            String numberOfAyasLabel = "Numri i ajeteve:";

            SurasItem item = new SurasItem(surahNumber, surahName, numberOfAyasLabel, numberOfAyas);
            quranSuras.add(item);
        }
        return quranSuras;
    }

    public static String ReadSurahTitle(Context context, int surahNumber) {

        InputSource is = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            is = new InputSource(context.getResources().getAssets().open("Albanian/ChapterNamesAlbanian.xml"));
            db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(is.getByteStream()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("Chapter");
        Element elmnt = null;
        for(int i = 0;i<=nodeList.getLength();i++)
        {
            Node n = nodeList.item(i);
            Element el = (Element) n;
            if (el.getAttribute("ChapterID").equals(Integer.toString(surahNumber))) {
                elmnt = el;
                break;
            }
        }
        //Node node = nodeList.item(surahNumber-1);
        //Element elmnt = (Element) node;
        String surahName = elmnt.getAttribute("ChapterName");

        return surahName;
    }

    public static int getNrOfAyasForSurah(int SurahNumber, Context context) {
        InputSource is = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            if (SurahNumber < 10) {
                is = new InputSource(context.getResources().getAssets().open("Albanian/Chapter00" + SurahNumber + ".xml"));
            } else if (SurahNumber >= 10 && SurahNumber < 100) {
                is = new InputSource(context.getResources().getAssets().open("Albanian/Chapter0" + SurahNumber + ".xml"));
            } else if (SurahNumber >= 100) {
                is = new InputSource(context.getResources().getAssets().open("Albanian/Chapter" + SurahNumber + ".xml"));
            }
            db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(is.getByteStream()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("Verse");

        return nodeList.getLength();
    }

    public static ArrayList<AyahItem> ReadAyasListXmlData(int SurahNumber, Context context) {
        InputSource is_arab = null;
        InputSource is_alb = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc_arab = null;
        Document doc_alb = null;
        try {
            if (SurahNumber < 10) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter00" + SurahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter00" + SurahNumber + ".xml"));
            } else if (SurahNumber >= 10 && SurahNumber < 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter0" + SurahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter0" + SurahNumber + ".xml"));
            } else if (SurahNumber >= 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter" + SurahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter" + SurahNumber + ".xml"));
            }
            db = dbf.newDocumentBuilder();
            doc_arab = db.parse(new InputSource(is_arab.getByteStream()));
            doc_alb = db.parse(new InputSource(is_alb.getByteStream()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc_arab.getDocumentElement().normalize();
        doc_alb.getDocumentElement().normalize();

        NodeList nodeListArab = doc_arab.getElementsByTagName("Verse");
        NodeList nodeListAlb = doc_alb.getElementsByTagName("Verse");

        ArrayList<AyahItem> quranAyas = new ArrayList<AyahItem>();

        for (int i = 0; i < nodeListArab.getLength(); i++) {
            Node nodeArab = nodeListArab.item(i);
            Element elmntArab = (Element) nodeArab;

            Node nodeAlb = nodeListAlb.item(i);
            Element elmntAlb = (Element) nodeAlb;

            String ayahNumberArab = elmntArab.getAttribute("VerseID").toString();
            String ayah_arab = elmntArab.getTextContent();

            String ayahNumberAlb = elmntAlb.getAttribute("VerseID").toString();
            String ayah_alb = elmntAlb.getTextContent();

            AyahItem item = new AyahItem(ayahNumberArab, ayah_arab,ayah_alb);
            quranAyas.add(item);
        }
        return quranAyas;
    }

    public static boolean SaveBookmarkedAyaToInternalStorage(String filename, Context context, String surahNumber, String AyahNumber) {

        try {
            File dir = context.getFilesDir();
            File file = new File(dir, filename);
            boolean deleted = file.delete();

            FileOutputStream fos;

            fos = context.openFileOutput(filename, Context.MODE_APPEND);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "Items");

            serializer.startTag(null, "SurahNumber");

            serializer.text(surahNumber);

            serializer.endTag(null, "SurahNumber");

            serializer.startTag(null, "AyahNumber");

            serializer.text(AyahNumber);

            serializer.endTag(null, "AyahNumber");

            serializer.endDocument();

            serializer.flush();

            fos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public static String ReadXmlFromInternalStorage(String filename, Context context)
    {
        String surahNumber = "",ayahNumber = "";
        try {
            //--- Testimi i leximit te xml fajllit si string gjate debuggimit
            /*
            StringBuilder sb = new StringBuilder();
            try {
                FileInputStream fin = context.openFileInput(filename);
                InputStreamReader isr = new InputStreamReader(fin);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String test = sb.toString();
             */
            //---

            InputStream is = context.openFileInput(filename);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            //---
            NodeList surahItems = doc.getElementsByTagName("SurahNumber");
            NodeList ayahItems = doc.getElementsByTagName("AyahNumber");
            if (surahItems.getLength() != 0)
            {
                 surahNumber = surahItems.item(0).getTextContent();
            }
            if (ayahItems.getLength() != 0)
            {
                ayahNumber = ayahItems.item(0).getTextContent();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return surahNumber+","+ayahNumber;
    }

    public static boolean checkIfElementExists(NodeList nl, String surahNumber, String ayahNumber)
    {
        boolean returnValue = false;
        for (int i=0;i<nl.getLength();i++)
        {
            Element item = (Element)nl.item(i);
            String SurahNumber = item.getElementsByTagName("SurahNumber").item(0).getTextContent();
            String AyahNumber = item.getElementsByTagName("AyahNumber").item(0).getTextContent();

            boolean checkSuras = SurahNumber.equals(surahNumber);
            boolean checkAyas = AyahNumber.equals(ayahNumber);
            if (checkSuras && checkAyas)
            {
                returnValue = true;
                break;
            }
            else
            {
                returnValue = false;
            }
        }
        return returnValue;
    }

    public static boolean SaveToSavingAyasToInternalStorage(String filename, Context context, String surahNumber, String AyahNumber) {
        boolean elementExists = false;
        try {

            /*File dir = context.getFilesDir();
            File file = new File(dir, filename);
            boolean fileExists = file.exists();*/

            if (Helper.fileExists(filename,context))
            {
                InputStream is = context.openFileInput(filename);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(is);

                Element root = doc.getDocumentElement();

                elementExists = checkIfElementExists(root.getElementsByTagName("Item"),surahNumber,AyahNumber);

                if (!elementExists) {

                    Element itemElement = doc.createElement("Item");
                    Element surahElement = doc.createElement("SurahNumber");
                    surahElement.setTextContent(surahNumber);
                    Element ayahElement = doc.createElement("AyahNumber");
                    ayahElement.setTextContent(AyahNumber);

                    itemElement.appendChild(surahElement);
                    itemElement.appendChild(ayahElement);

                    root.appendChild(itemElement);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource dSource = new DOMSource(doc);
                    StreamResult result = new StreamResult(context.openFileOutput(filename, Context.MODE_PRIVATE));
                    transformer.transform(dSource, result);
                }
            }
            else
            {
                FileOutputStream fos;

                fos = context.openFileOutput(filename, Context.MODE_APPEND);
                XmlSerializer serializer = Xml.newSerializer();
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument(null, Boolean.valueOf(true));
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

                serializer.startTag(null, "Savings");
                serializer.startTag(null, "Item");

                serializer.startTag(null, "SurahNumber");

                serializer.text(surahNumber);

                serializer.endTag(null, "SurahNumber");

                serializer.startTag(null, "AyahNumber");

                serializer.text(AyahNumber);

                serializer.endTag(null, "AyahNumber");

                serializer.endTag(null, "Item");
                serializer.endDocument();

                serializer.flush();

                fos.close();
                elementExists = false;

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return elementExists;
    }

    ///-- Per testim
    public static String ReadXmlForTest(Context context, String filename)
    {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fin = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String test = sb.toString();
        return test;
    }
    ///

    public static ArrayList<SurahAyahItem> readSavingsAyas(String filename,Context context)
    {
        ArrayList<SurahAyahItem> surasAyas = new ArrayList<SurahAyahItem>();

        InputStream is = null;
        Document doc = null;
        DocumentBuilder dBuilder = null;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {
            is = context.openFileInput(filename);
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(is);
            NodeList Items = doc.getElementsByTagName("Item");
            for (int i = 0; i<Items.getLength(); i++)
            {
                Element item = (Element)Items.item(i);

                Element surahItem = (Element) item.getElementsByTagName("SurahNumber").item(0);
                Element ayahItem = (Element) item.getElementsByTagName("AyahNumber").item(0);

                //String s1 = surahItem.getTextContent();
                //String s2 = ayahItem.getTextContent();

                SurahAyahItem sai = new SurahAyahItem(surahItem.getTextContent().toString(),ayahItem.getTextContent());
                surasAyas.add(sai);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Collections.reverse(surasAyas);
        return surasAyas;
    }

    public static boolean fileExists(String filename,Context context)
    {
        File dir = context.getFilesDir();
        File file = new File(dir, filename);
        boolean fileExists = file.exists();
        return fileExists;
    }

    public static AyahItem ReadSpecificAyah(Context context, int surahNumber, int ayahNumber) {

        InputSource is_arab = null;
        InputSource is_alb = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc_arab = null;
        Document doc_alb = null;
        try {
            if (surahNumber < 10) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter00" + surahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter00" + surahNumber + ".xml"));
            } else if (surahNumber >= 10 && surahNumber < 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter0" + surahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter0" + surahNumber + ".xml"));
            } else if (surahNumber >= 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter" + surahNumber + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter" + surahNumber + ".xml"));
            }
            db = dbf.newDocumentBuilder();
            doc_arab = db.parse(new InputSource(is_arab.getByteStream()));
            doc_alb = db.parse(new InputSource(is_alb.getByteStream()));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc_arab.getDocumentElement().normalize();
        doc_alb.getDocumentElement().normalize();

        NodeList nodeListArab = doc_arab.getElementsByTagName("Verse");
        NodeList nodeListAlb = doc_alb.getElementsByTagName("Verse");

        Element elmntArab = null;
        Element elmntAlb = null;
        for(int i = 0;i<=nodeListArab.getLength();i++)
        {
            Node nArab = nodeListArab.item(i);
            Node nAlb = nodeListAlb.item(i);

            Element elArab = (Element) nArab;
            Element elAlb = (Element) nAlb;
            //String elementeee = el.getAttribute("VerseID");

            if (elArab.getAttribute("VerseID").equals(Integer.toString(ayahNumber))) {
                elmntArab = elArab;
                elmntAlb = elAlb;
                break;
            }
        }

        //String surahName = elmnt.getAttribute("ChapterName");
        AyahItem aItem = new AyahItem(Integer.toString(surahNumber)+" : " + Integer.toString(ayahNumber),elmntArab.getTextContent(),elmntAlb.getTextContent());
        return aItem;
    }
    public static void showInformationDialog(String text, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static void deleteFile(Context context, String filename)
    {
        File dir = context.getFilesDir();
        File file = new File(dir, filename);
        file.delete();
    }
    public static Bitmap getRecyclerViewScreenshot(RecyclerView view) {
        int size = view.getAdapter().getItemCount();
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, 0);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
        Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), holder.itemView.getMeasuredHeight() * size,
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        bigCanvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        int iHeight = 0;
        holder.itemView.setDrawingCacheEnabled(true);
        holder.itemView.buildDrawingCache();
        bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
        holder.itemView.setDrawingCacheEnabled(false);
        holder.itemView.destroyDrawingCache();
        iHeight += holder.itemView.getMeasuredHeight();
        for (int i = 1; i < size; i++) {
            view.getAdapter().onBindViewHolder(holder, i);
            holder.itemView.setDrawingCacheEnabled(true);
            holder.itemView.buildDrawingCache();
            bigCanvas.drawBitmap(holder.itemView.getDrawingCache(), 0f, iHeight, paint);
            iHeight += holder.itemView.getMeasuredHeight();
            holder.itemView.setDrawingCacheEnabled(false);
            holder.itemView.destroyDrawingCache();
        }
        return bigBitmap;
    }

    public static void openFile(Context context,File url) {

        try {

            if (url.exists()) //Checking if the file exists or not
            {
            Uri uri =  FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",url);//Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }
            context.startActivity(intent);
            } else {

                Toast.makeText(context, "The file does not exists! ", Toast.LENGTH_SHORT).show();

            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public static ArrayList<String> ReadSearchedAyas(Context context,String searchText) {
        InputSource is_arab = null;
        InputSource is_alb = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc_arab = null;
        Document doc_alb = null;
        ArrayList<String> quranAyas = new ArrayList<String>();
        try {
            for(int i = 1; i <= 114; i++)
            {
            if (i < 10) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter00" + i + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter00" + i + ".xml"));
            } else if (i >= 10 && i < 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter0" + i + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter0" + i + ".xml"));
            } else if (i >= 100) {
                is_arab = new InputSource(context.getResources().getAssets().open("Arabic/Chapter" + i + ".xml"));
                is_alb = new InputSource(context.getResources().getAssets().open("Albanian/Chapter" + i + ".xml"));
            }
            db = dbf.newDocumentBuilder();
            doc_arab = db.parse(new InputSource(is_arab.getByteStream()));
            doc_alb = db.parse(new InputSource(is_alb.getByteStream()));


        doc_arab.getDocumentElement().normalize();
        doc_alb.getDocumentElement().normalize();

        NodeList nodeListArab = doc_arab.getElementsByTagName("Verse");
        NodeList nodeListAlb = doc_alb.getElementsByTagName("Verse");


        for (int j = 0; j < nodeListArab.getLength(); j++) {
            Node nodeArab = nodeListArab.item(j);
            Element elmntArab = (Element) nodeArab;

            Node nodeAlb = nodeListAlb.item(j);
            Element elmntAlb = (Element) nodeAlb;

            String ayahNumberArab = elmntArab.getAttribute("VerseID").toString();
            String ayah_arab = elmntArab.getTextContent();

            String ayahNumberAlb = elmntAlb.getAttribute("VerseID").toString();
            String ayah_alb = elmntAlb.getTextContent();
            if (!searchText.equals("") && ayah_alb.contains(searchText)) {
                quranAyas.add(i+":"+ayahNumberAlb+" - "+ayah_alb);
            }
        }
        }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quranAyas;
    }
}
