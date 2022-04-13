package com.example.ModelView.entities.web;

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
public class PrintModelOthWeb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String othName;

    private String othFormat;

    private Double othSize;

    @Lob
    private String previewOth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintModelOthWeb that = (PrintModelOthWeb) o;
        return othName.equals(that.othName) && othSize.equals(that.othSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(othName, othSize);
    }
}
