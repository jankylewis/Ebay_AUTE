package se.credentialFactory;

import org.yaml.snakeyaml.Yaml;
import se.utility.GlobalVariableUtil;
import se.utility.fileUtil.fileReaderUtil.YamlFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApiCredentialFactory {

    private YamlFileReader _yamlFileReader;

    public ApiCredentialFactory(){
        if (_yamlFileReader == null)
            _yamlFileReader = new YamlFileReader();
    }

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

    public Map<String, List<Map<String, String>>> az() throws FileNotFoundException {

        _yamlFileReader.setYamlPath(_apiYamlPath);

        Map<String, List<Map<String, String>>> d =
                (Map<String, List<Map<String, String>>>) _yamlFileReader.retrieveYamlMap();

        return d;
    }

    public Map<String, String> retrieveAnApiCredential(String identificationUsed) throws FileNotFoundException {

        _yamlFileReader.setYamlPath(_apiYamlPath);
        _apiData = (Map<String, List<Map<String, String>>>) _yamlFileReader.retrieveYamlMap();
        _apiCredentials = _apiData.get("api_credentials");

        Map<String, String> mappedCredential = _apiCredentials.stream()
                .filter(apiCred -> Objects.equals(apiCred.get("identification"), identificationUsed))
                .findFirst()
                .get();

        return mappedCredential;
    }

    public static void main (String[] args) throws FileNotFoundException {
        Map<String, String> data = new ApiCredentialFactory().retrieveAnApiCredential(GlobalVariableUtil.ApiRunConfigs.IDENTIFICATION_USED);
        int i = 1;
    }
}
