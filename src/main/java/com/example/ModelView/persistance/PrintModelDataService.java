package com.example.ModelView.persistance;

import com.example.ModelView.model.entities.locale.PrintModelData;
import com.example.ModelView.model.entities.locale.PrintModelZipData;
import com.example.ModelView.persistance.repositories.locale.ModelRepository;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryTags;
import com.example.ModelView.persistance.repositories.locale.ModelRepositoryZip;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrintModelDataService {

    private final ModelRepository modelRepository;
    private final ModelRepositoryZip modelRepositoryZip;
    private final ModelRepositoryTags modelRepositoryTags;

    public List<String> getAllNameTags() {
        return modelRepositoryTags.getAllNamesTags();
    }

    public List<PrintModelData> findAll() {
        return modelRepository.findAll();
    }

    public Page<PrintModelData> findAllWithSpecs(Specification<PrintModelData> modelSpecification, Pageable pageable) {
        return modelRepository.findAll(modelSpecification, pageable);
    }

    public Page<PrintModelZipData> findAllZips(Pageable pageable) {
        return modelRepositoryZip.findAll(pageable);
    }

    public void deleteById(Long id) {
        modelRepository.deleteById(id);
    }

    public Optional<PrintModelData> findById(Long id) {
        return modelRepository.findById(id);
    }

    public Page<PrintModelData> findAllByModelTagsObj_TagContaining(String tag, Pageable pageable) {
        return modelRepository.findAllByModelTagsObjData_TagContaining(tag, pageable);
    }
}
