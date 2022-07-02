package com.example.ModelView.model.entities.web;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "print_model_oth_web")
@Getter
@Setter
@NoArgsConstructor
public class PrintModelOthWebData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "oth_name")
    private String othName;
    @Column(name = "oth_format")
    private String othFormat;
    @Column(name = "oth_size")
    private Double othSize;
    @Lob
    @Column(name = "oth_preview")
    private String previewOth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelOthWebData that = (PrintModelOthWebData) o;
        return othName.equals(that.othName) && othSize.equals(that.othSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(othName, othSize);
    }
}
