package com.example.ModelView.persistance;

import com.example.ModelView.model.entities.web.PrintModelWebData;
import com.example.ModelView.persistance.repositories.locale.ModelRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryTags;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryZip;
import com.example.ModelView.persistance.repositories.web.ModelRepositoryTagsWeb;
import com.example.ModelView.persistance.repositories.web.ModelRepositoryWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebPrintModelDataService {

    private final ModelRepositoryWeb modelRepository;
    private final ModelRepositoryTagsWeb modelRepositoryTags;

    @Transactional(readOnly = true)
    public Page<PrintModelWebData> findAllWithSpecs(Specification<PrintModelWebData> searchSpec, Pageable pageable) {
        return modelRepository.findAll(searchSpec, pageable);
    }

    @Transactional(readOnly = true)
    public List<String> getAllNameTags() {
        return modelRepositoryTags.getAllNameTags();
    }

    public void deleteById(Long id) {
        modelRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PrintModelWebData> findAllByModelTagsObj_TagContaining(String tag, Pageable pageable) {
        return modelRepository.findAllByModelTags_NameTagContaining(tag, pageable);
    }

    public Optional<PrintModelWebData> findById(Long id) {
        return modelRepository.findById(id);
    }
}
