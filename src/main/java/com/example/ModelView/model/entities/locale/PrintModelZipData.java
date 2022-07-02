package com.example.ModelView.model.entities.locale;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@Setter
@Getter
public class PrintModelZipData implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_model_oth")
    private String nameModelZip;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "file_class")
    private String fileClass;
    @Column(name = "zip_adress")
    private String zipAddress;
    @Column(name = "zip_format")
    private String zipFormat;
    @Column(name = "zip_size")
    private Double zipSize;
    @Column(name = "archive_ratio")
    private Integer archiveRatio;



    public PrintModelZipData(String nameModelZip, String modelName, String zipAddress, String zipFormat, Double zipSize, int archiveRatio){
        this.nameModelZip = nameModelZip;
        this.modelName = modelName;
        this.fileClass = "ZIP";
        this.zipAddress = zipAddress;
        this.zipFormat = zipFormat;
        this.zipSize = zipSize;
        this.archiveRatio = archiveRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelZipData printModelZipData = (PrintModelZipData) o;
        return Double.compare(printModelZipData.archiveRatio, archiveRatio) == 0
                && nameModelZip.equals(printModelZipData.nameModelZip)
                && modelName.equals(printModelZipData.modelName)
                && fileClass.equals(printModelZipData.fileClass)
                && zipAddress.equals(printModelZipData.zipAddress)
                && zipFormat.equals(printModelZipData.zipFormat)
                && zipSize.equals(printModelZipData.zipSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameModelZip, modelName, fileClass, zipAddress, zipFormat, zipSize, archiveRatio);
    }
}
