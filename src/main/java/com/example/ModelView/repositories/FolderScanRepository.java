package com.example.ModelView.repositories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
@Getter
@Setter
public class FolderScanRepository {

    @Value("${scan.adress1}")
    private String adress1;

    @Value("${scan.adress2}")
    private String adress2;

    @Value("${scan.adress3}")
    private String adress3;

    @Value("${scan.adressSer}")
    private String adressSer;


    public Collection<File> startScanRepository(Boolean mode)  {

        Collection<File> files = new ArrayList<>(10000);
        Collection<File> files2 = new ArrayList<>(10000);
        Collection<File> files3 = new ArrayList<>(10000);

        long start = System.currentTimeMillis();
        if(mode) {
            File input1 = new File(adress1);
            File input2 = new File(adress2);
            File input3 = new File(adress3);
            try {
                files = FileUtils.streamFiles(input1, true, null).collect(Collectors.toList());
                files2 = FileUtils.streamFiles(input2, true, null).collect(Collectors.toList());
                files3 = FileUtils.streamFiles(input3, true, null).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            files.addAll(files2);
            files.addAll(files3);
        } else {
            File inpuSer = new File(adressSer);
            try {
                files = FileUtils.streamFiles(inpuSer, true, null).collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long fin = System.currentTimeMillis();
        System.out.println("ScanRepository SIZE " + files.size());
        System.out.println("ScanRepository TIME " + (fin - start));
        return files;
    }

}

// VER 1 TEST
// 1 ScanRepository SIZE 5354 ScanRepository TIME 7262 - 2 ScanRepository SIZE 5354 ScanRepository TIME 188
//                                                       3                          ScanRepository TIME 210
// 4 filesList size - 5354 printModelsList size - 1172  startCreateController time create - 199932
// 5                                                    startCreateController time create - 82257
// 6                                                    startCreateController time create - 86004
// 7                                                    startCreateController time create - 84278

// VER 2 TEST
// 1                                                    startCreateController time create - 4387
//   modelRepositoryJPA.saveAll time - 1175
//   modelRepositoryZIPJPA.saveAll time - 44
//   modelRepositoryOTHJPA.saveAll time - 49
//   ALL SAVE saveAllListToJpaRepository time - 1268
// 2                                                    startCreateController time create - 4334


//all folder
// 3 ScanRepository SIZE 24982 ScanRepository TIME 29750 - startCreateController time create - 381979
//   modelRepositoryJPA.saveAll time - 4721
//   modelRepositoryZIPJPA.saveAll time - 186
//   modelRepositoryOTHJPA.saveAll time - 204
//   ALL SAVE saveAllListToJpaRepository time - 5111
//   Входные файлы filesList size - 24982
//   Итоговые модели printModelsList size - 4044

// VER 2.1
//all folder (Win Defender on)
//1 ScanRepository SIZE 24844 ScanRepository TIME 37691 - startCreateController time create - 1036331 - 17.2 min
//  modelRepositoryJPA.saveAll time - 4914
//  modelRepositoryZIPJPA.saveAll time - 167
//  modelRepositoryOTHJPA.saveAll time - 212
//  ALL SAVE saveAllListToJpaRepository time - 5294
//  Входные файлы filesList size - 24844
//  Итоговые модели printModelsList size - 4041
//
// (Win Defender off second start)
//2 ScanRepository SIZE 24844 ScanRepository TIME 1308  - startCreateController time create - 38617
//  modelRepositoryJPA.saveAll time - 4251
//  modelRepositoryZIPJPA.saveAll time - 171
//  modelRepositoryOTHJPA.saveAll time - 182
//  ALL SAVE saveAllListToJpaRepository time - 4605
//3                                                     - startCreateController time create - 23949
//4 ScanRepository SIZE 24844 ScanRepository TIME 27700 - startCreateController time create - 458782
//5                           ScanRepository TIME 1071  - startCreateController time create - 20572
//6 ScanRepository SIZE 24844 ScanRepository TIME 44235 - startCreateController time create - 486660
//7                                                     - startCreateController time create - 445717

// VER 3 MultiThread Stream (X36)(bug)
//1 second start 1.5 TB                                 - startCreateController time create - 12245
// modelRepositoryJPA.saveAll time - 4595
// modelRepositoryZIPJPA.saveAll time - 204
// modelRepositoryOTHJPA.saveAll time - 217
// ALL SAVE saveAllListToJpaRepository time - 5016
//2                                                     - startCreateController time create - 12308
//3 ScanRepository SIZE 25080 ScanRepository TIME 51818 - startCreateController time create - 520911 (7,8 min alg)

// VER 3 True MultiThread Stream