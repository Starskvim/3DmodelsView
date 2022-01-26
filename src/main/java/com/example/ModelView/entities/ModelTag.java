package com.example.ModelView.entities;


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

    @ManyToMany
    private List<PrintModel> printModels = new ArrayList<>();

    public ModelTag (String tag) {
        this.tag = tag;
    }

    public void addModelInTag (PrintModel printModel) {
        printModels.add(printModel);
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
