package com.example.ModelView.services.create;

import com.example.ModelView.entities.ModelTag;
import com.example.ModelView.repositories.jpa.ModelRepositoryTagsJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
class EntitiesAttributeServiceTest {
    EntitiesAttributeService entitiesAttributeService;
    @Mock
    CollectionsService collectionsService;
    @Mock
    ModelRepositoryTagsJPA modelRepositoryTagsJPA;
    @Mock
    HashSet<ModelTag> modelsTagsSavedSet = new HashSet<>();
    @Mock
    CopyOnWriteArraySet<ModelTag> modelsTagsToSaveSet = new CopyOnWriteArraySet<>();

    @BeforeEach
    void initService(){
        entitiesAttributeService = new EntitiesAttributeService(collectionsService, modelRepositoryTagsJPA);
    }

    @Test
    void detectCreateObjTag() {
        entitiesAttributeService.setModelsTagsSavedSet(modelsTagsSavedSet);
        entitiesAttributeService.setModelsTagsToSaveSet(modelsTagsToSaveSet);
        String[] tests = new String[]{
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Pack]\\[[Star Wars]]\\2",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Figure]\\[[Star Wars]]\\[Ashoka SW]\\2",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Other]\\1",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Pack]\\[[Terrain+Acces+M]]\\[Mech Warrior Builder Kit]\\2",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Pack]\\[Asgard Rising]\\+Asgard Rising April 2020\\1",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Pack]\\[Clay Cyanide]\\1",
                "F:\\[3D PRINT]\\Модели\\[Patreon]\\[Figure]\\[Rushzilla]\\1"
        };
        for (String path: tests) {
            entitiesAttributeService.detectCreateObjTag(path);
        }
        assertEquals(10,entitiesAttributeService.getModelsTagsToSaveSet().size());
    }

    @Test
    void getSizeFileToDouble(){
        File testFile = mock(File.class);
        when(testFile.length()).thenReturn(8388608L).thenReturn(15728640L);
        assertEquals(8.0, entitiesAttributeService.getSizeFileToDouble(testFile));
        assertEquals(15.0, entitiesAttributeService.getSizeFileToDouble(testFile));
    }
}