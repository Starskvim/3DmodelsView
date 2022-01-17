package com.example.ModelView.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.*;
import java.util.*;


@Entity
@Table(name = "MODELPRINT")
@NoArgsConstructor
@Setter
@Getter
public class PrintModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelName;

    private String modelDerictory;

    private String modelCategory;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<ModelZIP> modelZIPSet = new HashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<ModelOTH> modelOTHSet = new HashSet<>();


    public PrintModel(String modelName, String modelDerictory, String modelCategory) {
        this.modelName = modelName;
        this.modelDerictory = modelDerictory;
        this.modelCategory = modelCategory;
    }

    public void addModelZIP(ModelZIP modelZIP) {
        modelZIPSet.add(modelZIP);
    }

    public void addModelOTH(ModelOTH modelOTH) {
        modelOTHSet.add(modelOTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModel that = (PrintModel) o;
        return modelName.equals(that.modelName)
                && modelDerictory.equals(that.modelDerictory)
                && modelCategory.equals(that.modelCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, modelDerictory, modelCategory);
    }
}