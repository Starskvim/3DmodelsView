package com.example.ModelView.entities.locale;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@RequiredArgsConstructor
@Setter
@Getter
public class ModelTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tag;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,
            mappedBy="modelTagsObj")
    private List<PrintModel> printModels = new ArrayList<>();

    public ModelTag (String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelTag modelTag = (ModelTag) o;
        return Objects.equals(tag, modelTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
