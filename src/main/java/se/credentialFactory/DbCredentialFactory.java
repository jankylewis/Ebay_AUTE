package se.credentialFactory;

import se.utility.fileUtil.fileReaderUtil.YamlFileReader;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbCredentialFactory {

    private final String _dbYamlPath = "./src/main/java/se/dataManager/accountManager/db_credential.yaml";

    private YamlFileReader _yamlFileReader;
    private Map<String, List<Map<String, String>>> _dbData;
    private List<Map<String, String>> _dbCredentials;

    public DbCredentialFactory(){
        if (_yamlFileReader == null)
            _yamlFileReader = new YamlFileReader();
    }

    public Map<String, String> retrieveADbCredential(String dbName) throws FileNotFoundException {

        _yamlFileReader.setYamlPath(_dbYamlPath);
        _dbData = (Map<String, List<Map<String, String>>>) _yamlFileReader.retrieveYamlMap();
        _dbCredentials = _dbData.get("db_credentials");

        Map<String, String> mappedDbCredential = _dbCredentials.stream()
                .filter(apiCred -> Objects.equals(apiCred.get("db_name"), dbName))
                .findFirst()
                .get();

        return mappedDbCredential;
    }

    public String retrieveDbConnectionString(String dbName) {

        Map<String, String> mappedDbCredential = _dbCredentials.stream()
                .filter(dbCred -> Objects.equals(dbCred.get("db_name"), dbName))
                .findFirst()
                .get();

        return mappedDbCredential.get("db_connection_string");
    }
}
