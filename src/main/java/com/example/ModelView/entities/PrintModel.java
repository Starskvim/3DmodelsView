package com.example.ModelView.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.*;
import java.util.*;


@Entity
@NamedEntityGraph(
        name = "PrintModel.all",
        attributeNodes = {
//                @NamedAttributeNode("modelTagsObj"),
//                @NamedAttributeNode("modelZIPSet"),
                @NamedAttributeNode("modelOTHSet")})
@Table
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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tags_and_models",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<ModelTag> modelTagsObj = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<ModelZIP> modelZIPSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<ModelOTH> modelOTHSet = new HashSet<>();


    public PrintModel(String modelName, String modelDerictory, String modelCategory) {
        this.modelName = modelName;
        this.modelDerictory = modelDerictory;
        this.modelCategory = modelCategory;

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