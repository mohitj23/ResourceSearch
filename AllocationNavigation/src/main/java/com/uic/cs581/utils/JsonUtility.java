package com.uic.cs581.utils;

import com.uic.cs581.model.CommonInterface;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonUtility {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <V extends CommonInterface> void writeToFile(String filePath, List<V> objs) {

        // Writing to a file
        try {
            mapper.writeValue(new File(filePath), objs);
        } catch (IOException e) {
            log.error("Error while writing JSON to file.");
        }
    }
}
