package com.example.ModelView.utillity;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.io.File;

@UtilityClass
public class CreateUtils {

    public static Double getSizeFileToDouble(File file) {
        double inputSize = file.length() / 1024.0 / 1024.0;
        double scale = Math.pow(10, 3);
        return Math.round(inputSize * scale) / scale;
    }

    public static String detectPrintModelCategory(File file) {
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

    public static String trimStringNameModel (String modelName){
        String modelNameOld = StringUtils.trimLeadingCharacter(modelName, '+');
        return StringUtils.trimTrailingWhitespace(modelNameOld);
    }

    public static Integer detectMyRateForModel(String nameFolderModel) {
        int myRate = StringUtils.countOccurrencesOf(nameFolderModel, "+");
        myRate = myRate == 1 ? 0 : Math.max(myRate - 1, 0);
        return myRate;
    }

}
