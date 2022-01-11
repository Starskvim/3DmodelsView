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

    public String getSizeFileToString (File file) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(file.length() / 1024.0 / 1024.0);
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

}
