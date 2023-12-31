package com.hook.hicodingapi.common.util;


/* 필요한 메소드 정의 위해 util 디렉토리에 만든다 */

import com.hook.hicodingapi.common.exception.ServerInternalException;
import com.hook.hicodingapi.file.domain.File;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.FAIL_TO_DELETE_FILE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.FAIL_TO_UPLOAD_FILE;


public class FileUploadUtils {

    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {

        try(InputStream inputStream = multipartFile.getInputStream()) {

            Path uploadPath = Paths.get(uploadDir);
            /* 업로드 경로가 존재하지 않을 시 경로 먼저 생성 */
            if(!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            /* 파일명 생성 */
            String replaceFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

            /* 파일 저장 */
            Path filePath = uploadPath.resolve(replaceFileName);  //경로와 파일명 합친걸 filePath에다가 저장
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return replaceFileName;

        } catch (IOException e) {
            throw new ServerInternalException(FAIL_TO_UPLOAD_FILE);
        }
    }

    public static void deleteFile(String uploadDir, String fileName) {

        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new ServerInternalException(FAIL_TO_DELETE_FILE);
        }


    }

}







