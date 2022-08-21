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
}
