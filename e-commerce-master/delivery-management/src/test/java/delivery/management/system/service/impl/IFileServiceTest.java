package delivery.management.system.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import common.exception.model.service.ExceptionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {IFileService.class})
@ExtendWith(SpringExtension.class)
class IFileServiceTest {
    @MockBean
    private ExceptionService exceptionService;

    @Autowired
    private IFileService iFileService;


    @Test
    @Disabled("TODO: Complete this test")
    void testUpload() {
        MultipartFile multipartFile = null;

        // Act
        this.iFileService.upload(multipartFile);
    }


    @Test
    void testFindByName() {
        // Arrange, Act and Assert
        assertTrue(iFileService.findByName(new ArrayList<>()).isEmpty());
    }
}
