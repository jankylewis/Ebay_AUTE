package se.utility.dbUtil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManipulationUtil {

    public DbManipulationUtil(){}

    private Statement _statement;
    private ResultSet _resultSet;

    private String _query;

    public String setQuery(String expQuery) {
        _query = expQuery;
        return expQuery;
    }

    public ResultSet executeQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _resultSet = _statement.executeQuery(_query);

        return _resultSet;
    }

    public <M> List<M> mapResultSetToModelList(@NotNull ResultSet resultSet, Class<M> expModelClass)
            throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<M> models = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];

        for (int idx = 1; idx <= columnCount; idx++) {
            columnNames[idx-1] = metaData.getColumnLabel(idx);
        }

        while (resultSet.next()) {

            M model = expModelClass.getDeclaredConstructor().newInstance();

            for (int idx = 0; idx < columnCount; idx++) {
                String columnName = columnNames[idx];

                Method setter =
                        expModelClass.getMethod("set" + capitalizeWording(columnName), getColumnTypeAsClass(metaData.getColumnType(idx+1)));

                setter.invoke(model, resultSet.getObject(idx+1));
            }

            models.add(model);
        }

        return models;
    }

    //region Private services

    private Statement prepareStatement(@NotNull Connection connection) throws SQLException {
        return _statement = connection.createStatement();
    }

    private @NotNull String capitalizeWording(@NotNull String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @Contract(pure = true)
    private @Nullable Class<?> getColumnTypeAsClass(int primitiveType) throws SQLDataException {

        return switch (primitiveType) {
            case Types.INTEGER -> Integer.class;
            case Types.VARCHAR -> String.class;
            default -> throw new SQLDataException(
                    "SQLDataException came up with an unsupported SQL data type: " + primitiveType);
        };
    }

    //endregion
}


