package com.example.ModelView.persistance;

import com.example.ModelView.model.entities.web.PrintModelWebData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //todo
@RequiredArgsConstructor
public class WebPrintModelDataService {


    public Page<PrintModelWebData> findAllWithSpecs(Specification<PrintModelWebData> searchSpec, Pageable pageable) {
       return null;
    }

    public List<String> getAllNameTags() {
        return null;
    }

    public void deleteById(Long id) {
    }

    public Page<PrintModelWebData> findAllByModelTagsObj_TagContaining(String tag, Pageable pageable) {
        return null;
    }

    public Optional<PrintModelWebData> findById(Long id) {
        return null;
    }
}
