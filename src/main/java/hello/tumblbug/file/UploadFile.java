package hello.tumblbug.file;

import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class UploadFile {

    private String uploadFileName;

    private String storeFileName;
}
