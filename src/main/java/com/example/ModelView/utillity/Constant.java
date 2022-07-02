package com.example.ModelView.utillity;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constant {

    @UtilityClass
    public class Create {

        public static List<String> ZIP_FORMATS = List.of("zip", "7z", "7zip", "rar", "ZIP", "RAR", "7ZIP");
        public static List<String> IMAGE_FORMATS = List.of("jpg", "jpeg", "png", "gif");
        public static List<String> NSFW_TRIGGERS = List.of("nsfw", "NSFW", "nude", "18+");

    }
}
