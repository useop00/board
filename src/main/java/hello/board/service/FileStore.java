package hello.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static org.springframework.util.StringUtils.cleanPath;

@Component
public class FileStore {

    @Value("${file.dir}")

    private String fileDir;


    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String oriImgName = cleanPath(file.getOriginalFilename());
        String savedFileName = createFileName(oriImgName);

        String savePath = Paths.get(fileDir, savedFileName).toString();

        file.transferTo(new File(savePath));

        return savedFileName;
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void deleteFile(String filePath) throws IOException {
        String fullPath = Paths.get(fileDir, filePath).toString();
        File file = new File(fullPath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("파일 삭제에 실패했습니다: " + fullPath);
            }
        }
    }
}
