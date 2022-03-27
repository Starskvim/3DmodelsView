package com.example.ModelView.controllers.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SearchRequestParams {

    public String wordName;
    public String wordCategory;

}
