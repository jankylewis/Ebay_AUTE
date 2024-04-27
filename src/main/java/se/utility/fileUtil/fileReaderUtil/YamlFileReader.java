package se.utility.fileUtil.fileReaderUtil;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class YamlFileReader {

    private Yaml _yaml;
    private InputStream _fileInputStream;

    private Map<String, List<Map<String, String>>> _apiData;
    private List<Map<String, String>> _apiCredentials;
    private final String _apiYamlPath = "./src/main/java/se/dataManager/accountManager/api_credential.yaml";

    public List<?> retrieveCredentials() throws FileNotFoundException {

        if (_yaml == null) _yaml = new Yaml();

        _fileInputStream = new FileInputStream(_apiYamlPath);
        _apiData = _yaml.load(_fileInputStream);
        _apiCredentials = _apiData.get("api_credentials");

        return _apiCredentials;
    }
}
