package com.example.ModelView.rest.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.isNull;

@Getter
public class PrintModelRequest <M, O, Z> {

    M printModel;
    Collection<O> printOths;
    Collection<Z> printZips;

    public PrintModelRequest (M printModel, Collection<O> printOths, Collection<Z> printZips) {
        this.printModel = printModel;
        setPrintOths(printOths);
        setPrintZips(printZips);
    }

    public void setPrintOths(Collection<O> printOths){
        if(isNull(printOths)){
            this.printOths = new ArrayList<O>();
        }
        this.printOths = printOths;
    }

    public void setPrintZips(Collection<Z> printZips){
        if(isNull(printZips)){
            this.printZips = new ArrayList<Z>();
        }
        this.printZips = printZips;
    }
}
