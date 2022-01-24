package com.example.ModelView.services;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Service
public class EntitiesAttributeService {

    private static volatile Boolean isSevenZipInitialized = false;

    public static void initSevenZip()
    {
        synchronized (isSevenZipInitialized)
        {
            boolean inited = isSevenZipInitialized;
            if (inited)
                return;
            try
            {
                SevenZip.initSevenZipFromPlatformJAR();
                isSevenZipInitialized = true;
                System.out.println("7zip initialized successfully!");
            }
            catch (SevenZipNativeInitializationException s)
            {
                System.out.println("Unable to initialize 7zip! " + s.getMessage());
            }
        }
    }

    public int getCreateArchiveCompressionRatio(String sourceZipFile) {

        initSevenZip();

        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

            double allsize = 0;
            double allOrigsize = 0;

            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                try {
                    allsize += item.getSize();
                    allOrigsize += item.getPackedSize();
                } catch (Exception e) {
                    allsize += 0.0;
                    allOrigsize += 0.0;
                }
            }
            double preResult = allOrigsize / allsize;

            //System.out.println(sourceZipFile +" - ok - "+ preResult);

            return (int) Math.round(preResult * 100.0); ////////
        } catch (Exception e) {
            System.err.println("Error occurs: " + e.getMessage() + "-----" + sourceZipFile);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    System.err.println("Error closing archive: " + e.getMessage());
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
        return 0;
    }

    public Double getSizeFileToString (File file) {
//        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        double inputSize = file.length() / 1024.0 / 1024.0;
        double scale = Math.pow(10, 3);
        return Math.round(inputSize * scale) / scale;
    }

    public String detectPrintModelCategory(File file) {
        if (file.getPath().contains("[Figure]")) {
            return "[Figure]";
        } else if (file.getPath().contains("[Pack]")) {
            return "[Pack]";
        } else if (file.getPath().contains("[Other]")) {
            return "[OtherFDM]";
        } else {
            return "Other";
        }
    }

    public ArrayList<String> detectTag (File file) {
        ArrayList<String> tags = new ArrayList<>();
        String inputString = file.getPath();
        String reg = "\\\\";
        String[] splitString = inputString.split(reg);
        for (String word : splitString) {
            if (word.contains("[") && !word.equals("[3D PRINT]") && !word.equals("[Patreon]")) {
                StringBuilder stringBuilder = new StringBuilder();
                int status = 0;
                for (char ch : word.toCharArray()) {
                    if (ch == '[') {
                        status++;
                    }
                    if(status > 0){
                        stringBuilder.append(ch);
                    }
                    if(ch == ']'){
                        status--;
                    }
                }
                tags.add(stringBuilder.toString());
            }
        }
        if(inputString.contains("NSFW")){
            tags.add("[NSFW]");
        }
        return tags;
    }

}
