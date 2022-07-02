package com.example.ModelView.model.entities.locale;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.*;
import java.util.Objects;

@Entity
@Table(name = "model_other_files")
@NoArgsConstructor
@Setter
@Getter
public class PrintModelOthData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_model_oth")
    private String nameModelOth;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "file_class")
    private String fileClass;
    @Column(name = "oth_address")
    private String othAddress;
    @Column(name = "oth_format")
    private String othFormat;
    @Column(name = "oth_size")
    private Double othSize;


    public PrintModelOthData(String nameModelOth, String modelName, String othAddress, String othFormat, Double othSize) {
        this.nameModelOth = nameModelOth;
        this.modelName = modelName;
        this.fileClass = "OTH";
        this.othAddress = othAddress;
        this.othFormat = othFormat;
        this.othSize = othSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelOthData printModelOthData = (PrintModelOthData) o;
        return nameModelOth.equals(printModelOthData.nameModelOth)
                && modelName.equals(printModelOthData.modelName)
                && fileClass.equals(printModelOthData.fileClass)
                && othAddress.equals(printModelOthData.othAddress)
                && othFormat.equals(printModelOthData.othFormat)
                && othSize.equals(printModelOthData.othSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameModelOth, modelName, fileClass, othAddress, othFormat, othSize);
    }
}
