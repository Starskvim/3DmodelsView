package com.example.ModelView.utillity;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

import static com.example.ModelView.utillity.Constant.Create.Category.*;

@UtilityClass
public class CreateUtils {

    public static Double getSizeFileToDouble(File file) {
        double inputSize = file.length() / 1024.0 / 1024.0;
        double scale = Math.pow(10, 3);
        return Math.round(inputSize * scale) / scale;
    }

    public static String detectPrintModelCategory(File file) {
        if (file.getPath().contains(FIGURE)) {
            return FIGURE;
        } else if (file.getPath().contains(PACK)) {
            return PACK;
        } else if (file.getPath().contains(OTHER_FDM)) {
            return OTHER_FDM;
        } else {
            return OTHER;
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

    public static boolean detectTrigger(String path, List<String> triggers) {
        for (String trigger: triggers) {
            if (path.contains(trigger)){
                return true;
            }
        }
        return false;
    }

}
