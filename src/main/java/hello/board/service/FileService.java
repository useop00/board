package hello.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStore fileStore;

    public String uploadFile(String originalFileName, byte[] fileData) throws IOException {
        MultipartFile multipartFile = new InMemoryMultipartFile(originalFileName, fileData);
        return fileStore.uploadFile(multipartFile);
    }

    public void deleteFile(String filePath) throws IOException {
        fileStore.deleteFile(filePath);
    }

    public String generateFileUrl(String filePath) {
        return fileStore.generateFileUrl(filePath);
    }

    private record InMemoryMultipartFile(String originalFileName, byte[] fileData) implements MultipartFile {

        @Override
        public String getName() {
            return originalFileName;
        }

        @Override
        public String getOriginalFilename() {
            return originalFileName;
        }

        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        @Override
        public boolean isEmpty() {
            return fileData.length == 0;
        }

        @Override
        public long getSize() {
            return fileData.length;
        }

        @Override
        public byte[] getBytes() {
            return fileData;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(fileData);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(fileData);
            }
        }
    }
}