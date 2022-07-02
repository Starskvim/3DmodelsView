package com.example.ModelView.model.entities.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@NamedEntityGraph(
        name = "TagWithModels",
        attributeNodes = {
                @NamedAttributeNode("printModels")})
@Table(name = "print_model_tag_web")
@RequiredArgsConstructor
@Setter
@Getter
public class PrintModelTagWebData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_tag")
    private String nameTag;
    @Column(name = "count_models")
    private Integer countModels = 0;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY,
            mappedBy="modelTags")
    private List<PrintModelWebData> printModels = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelTagWebData modelTag = (PrintModelTagWebData) o;
        return Objects.equals(nameTag, modelTag.nameTag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTag);
    }
}
