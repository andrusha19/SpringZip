package site;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.zip.ZipOutputStream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping(value="/")
public class MyController {

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value="/zip_file", method = RequestMethod.POST)
    public ResponseEntity<byte[]> zipFile(@RequestParam MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        if (file.isEmpty()){
            headers.add("Location", "/SpringZip");
            return new ResponseEntity<>(null, headers, HttpStatus.FOUND);
        }
        byte[] array;
        try {
            array = file.getBytes();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                ZipEntry ze = new ZipEntry(file.getOriginalFilename());
                ze.setSize(array.length);
                zos.putNextEntry(ze);
                zos.write(array);
                zos.closeEntry();
            }
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Content-Disposition","attachment; filename=\""
                                + file.getOriginalFilename() + ".zip\"");
            return new ResponseEntity<>(bos.toByteArray(), headers,HttpStatus.OK);
        } catch (IOException e) {
            headers.add("Location", "/SpringZip");
            return new ResponseEntity<>(null, headers, HttpStatus.FOUND);
        }
    }
}
