package com.example.ModelView.entities.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedEntityGraph(
        name = "ForPrintModelPage",
        attributeNodes = {
                @NamedAttributeNode("modelOthList")})
@NamedEntityGraph(
        name = "ForPrintModelPreview",
        attributeNodes = {
                @NamedAttributeNode("previewModel")})
@Table(name = "print_model_web")
@Getter
@Setter
@NoArgsConstructor
public class PrintModelWeb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelName;

    private Double modelSize;

    private String modelCategory;

    private String modelPath;

    private Long views;

    private Integer myRate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tags_and_models_web",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<PrintModelTagWeb> modelTags = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn
    private PrintModelOthWeb previewModel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private List<PrintModelOthWeb> modelOthList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelWeb that = (PrintModelWeb) o;
        return modelName.equals(that.modelName) && modelSize.equals(that.modelSize) && modelCategory.equals(that.modelCategory) && modelPath.equals(that.modelPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, modelSize, modelCategory, modelPath);
    }
}
