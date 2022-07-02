package com.example.ModelView.rest.request;

import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.model.rest.PrintModelOth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrintModelRequest <M> {

    M printModel;
    Collection<PrintModelZipData> printZips;
    Collection<PrintModelOth> printOths;

}
