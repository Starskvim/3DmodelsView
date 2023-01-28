package com.example.ModelView.utillity;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constant {

    @UtilityClass
    public class Create {

        public static List<String> ZIP_FORMATS = List.of(
                "zip", "7z", "7zip", "rar", "ZIP", "RAR", "7ZIP"
        );
        public static List<String> IMAGE_FORMATS_TRIGGERS = List.of(
                "jpg", "jpeg", "png", "gif", "JPG", "JPEG", "PNG", "GIF"
        );
        public static List<String> NSFW_TRIGGERS = List.of(
                "nsfw", "NSFW", "Nsfw", "Nude", "NUDE", "nude", "18+", "Adult", "adult", "ADULT",
                "Digital Dark Pin-Ups", "Dungeon Pin-ups", "Pink Studio", "Rushzilla", "Rubim", "Texelion"
        );

        @UtilityClass
        public class Category {

            public static String FIGURE = "[Figure]";
            public static String PACK = "[Pack]";
            public static String OTHER_FDM = "[OtherFDM]";
            public static String OTHER = "[Other]";
        }
    }

    @UtilityClass
    public class Service {

        public static String EMPTY_STRING = "";
        public static String JSON_FORMAT = ".json";
    }

    @UtilityClass
    public class Log {

        public static String START_PROGRESS_BAR = "Start Progress Bar";

        public static String CREATE_SELECT = "Create select PrintModel {} Time {}";
        public static String CREATE_PAGE = "Create page {} Time {}";
        public static String CREATE_PAGE_TAG = "Create page modelTagList {} Time {}";
        public static String SCAN_TIME = "Scan time {}";

        public static String POST_GET = "Post get - {}";
        public static String POST_SYNC_GET = "PostSync get - {}";

        public static String CREATE_POST = "Create post - {}; url - {}";

        public static String LIST_LOCAL_SIZE = "ListLocalModels size - {}";
        public static String LIST_LOCAL_AFTER_SIZE = "ListLocalModels after size - {}";

        public static String INPUT_FILES = "Input files filesList size - {}";
        public static String RESULT_MODELS = "Result models printModelsList size - {}";
        public static String MODEL_CREATE = "Model - create - {}";
    }
}
