package se.utility.dbUtil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.SQLException;
import java.sql.SQLDataException;
import java.sql.Types;

//This class is responsible for processing the returned results
public class DbResultProcessing {

    public DbResultProcessing(){}

    private ResultSet _resultSet;

    public <M> List<M> mapResultSetToModelList(@NotNull ResultSet resultSet, Class<M> expModelClass)
            throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        _resultSet = resultSet;

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

    //region Public services

    public int getNumberOfRecords(@NotNull ResultSet resultSet) throws SQLException {

        int recordCount = 0;
        while(resultSet.next()) recordCount++;

        if (recordCount > 0) return recordCount;
        return -1;
    }

    public int getNumberOfRecords(@NotNull ResultSet resultSet, int resultSetType) throws SQLException {

        if (!(resultSet.getType() == (resultSetType)))
            throw new SQLException("Scrollable ResultSet was not supported!     ");

        int recordCount = 0;
        while (resultSet.next()) {
            recordCount++;
        }

        //Moving ResultSet cursor onto the first row
        resultSet.beforeFirst();

        if (recordCount > 0) return recordCount;
        return -1;
    }

    public void moveCursorOntoFirstRow(ResultSet resultSet) throws SQLException {

        if (!(resultSet.getType() == ResultSet.TYPE_SCROLL_SENSITIVE))
            throw new SQLException("Scrollable ResultSet was not supported!     ");

        resultSet.beforeFirst();
    }

    //endregion Public services

    //region Internal services

    private @NotNull String capitalizeWording(@NotNull String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @Contract(pure = true)
    private @Nullable Class<?> getColumnTypeAsClass(int dataType) throws SQLDataException {

        return switch (dataType) {

            case Types.INTEGER -> Integer.class;
            case Types.VARCHAR -> String.class;
            case Types.TIMESTAMP -> Date.class;

            default -> throw new
                    SQLDataException("SQLDataException came up with an unsupported SQL data type: " + dataType);
        };
    }

    protected DbResultProcessing disposeResultSetService() throws SQLException {
        _resultSet.close();
        return this;
    }

    //endregion Internal services
}
