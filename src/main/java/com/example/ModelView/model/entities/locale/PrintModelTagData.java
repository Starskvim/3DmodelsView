package com.example.ModelView.model.entities.locale;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "print_model_tag_web")
@RequiredArgsConstructor
@Setter
@Getter
public class PrintModelTagData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_name")
    private String tag;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY,
            mappedBy= "modelTagsObjData")
    private List<PrintModelData> printModelData = new ArrayList<>();

    public PrintModelTagData(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelTagData printModelTagData = (PrintModelTagData) o;
        return Objects.equals(tag, printModelTagData.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
