package com.example.ModelView.model.entities.locale;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Entity
@NamedEntityGraph(
        name = "PrintModel-oth",
        attributeNodes = {
                @NamedAttributeNode("printModelOthDataSet")})
@NamedEntityGraph(
        name = "PrintModel-all",
        attributeNodes = {
                @NamedAttributeNode("modelTagsObjData"),
                @NamedAttributeNode("printModelZipDataSet"),
                @NamedAttributeNode("printModelOthDataSet")})
@Table(name = "print_model")
@NoArgsConstructor
@Setter
@Getter
public class PrintModelData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "model_directory")
    private String modelDirectory;
    @Column(name = "model_category")
    private String modelCategory;
    @Column(name = "my_rate")
    private Integer myRate = 0;
    @Column(name = "nsfw")
    private Boolean nsfw = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tags_and_models",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<PrintModelTagData> modelTagsObjData = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<PrintModelZipData> printModelZipDataSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private Set<PrintModelOthData> printModelOthDataSet = new HashSet<>();


    public PrintModelData(String modelName, String modelDirectory, String modelCategory) {
        this.modelName = modelName;
        this.modelDirectory = modelDirectory;
        this.modelCategory = modelCategory;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelData that = (PrintModelData) o;
        return modelName.equals(that.modelName)
                && modelDirectory.equals(that.modelDirectory)
                && modelCategory.equals(that.modelCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, modelDirectory, modelCategory);
    }
}