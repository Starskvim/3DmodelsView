package com.example.ModelView.model.entities.web;

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
public class PrintModelWebData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "model_name")
    private String modelName;
    @Column(name = "model_size")
    private Double modelSize;
    @Column(name = "model_category")
    private String modelCategory;
    @Column(name = "model_path")
    private String modelPath;
    @Column(name = "views")
    private Long views;
    @Column(name = "my_rate")
    private Integer myRate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tags_and_models_web",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<PrintModelTagWebData> modelTags = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn
    private PrintModelOthWebData previewModel;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn
    private List<PrintModelOthWebData> modelOthList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelWebData that = (PrintModelWebData) o;
        return modelName.equals(that.modelName) && modelSize.equals(that.modelSize) && modelCategory.equals(that.modelCategory) && modelPath.equals(that.modelPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, modelSize, modelCategory, modelPath);
    }
}
