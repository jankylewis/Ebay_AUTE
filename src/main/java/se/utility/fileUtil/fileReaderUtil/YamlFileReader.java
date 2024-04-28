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
    private Map<?, ?> _dataMap;
    private String _yamlPath;

    public YamlFileReader(){
        if (_yaml == null)
            _yaml = new Yaml();
    }

    public void setYamlPath(String yamlPath) {
        _yamlPath = yamlPath;
    }

    public Map<?, ?> retrieveYamlMap() throws FileNotFoundException {

        _fileInputStream = new FileInputStream(_yamlPath);
        _dataMap = _yaml.load(_fileInputStream);

        return _dataMap;
    }
}
