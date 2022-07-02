package com.example.ModelView.rest.request;

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
