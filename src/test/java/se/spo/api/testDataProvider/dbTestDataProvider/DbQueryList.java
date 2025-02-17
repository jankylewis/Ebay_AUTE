package se.spo.api.testDataProvider.dbTestDataProvider;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DbQueryList {

    protected DbQueryList(){}

    //region SELECT queries

    protected final String SELECT_ALL = "SELECT * FROM userAuthenticationTb";
    protected final String SELECT_RECORD_1UP = "SELECT clientId FROM userAuthenticationTb WHERE (SELECT COUNT(clientId) FROM userAuthenticationTb) > 0";

    //endregion SELECT queries

    //region INSERT queries

    protected final @NotNull String INSERT_VALUES(String table, @NotNull List<String> columns, List<Object> columnData){

        String tableColumnsString = "(";
        String tableDataString = "(";

        for (int idx = 0; idx < columns.size(); idx++){

            if (idx == columns.size() - 1) {
                tableColumnsString += columns.get(idx) + ") ";
                break;
            }

            tableColumnsString += columns.get(idx) + ", ";
        }

        for (int idx = 0; idx < columnData.size(); idx++){

            if (idx == columnData.size() - 1) {
                tableDataString += handleApostrophe(columnData.get(idx)) + ")";
                break;
            }

            tableDataString += handleApostrophe(columnData.get(idx)) + ", ";
        }

        return "INSERT INTO " + table + tableColumnsString + "VALUES "+ tableDataString;
    }

    //endregion INSERT queries

    //region UPDATE queries

    @Contract(pure = true)
    protected final @NotNull String UPDATE(
            String table, String updatedColumn, String columnAtCondition, Object updatedValue, Object condition) {

        return "UPDATE " + table + " SET " + updatedColumn + " = " + updatedValue + " WHERE " + columnAtCondition + " = " + handleApostrophe(condition);
    }

    protected final @NotNull String UPDATE(
            String table, @NotNull List<String> updatedColumns, String columnAtCondition, List<Object> updatedValues, Object condition) {

        String setClause = " SET ";
        for (int idx = 0; idx < updatedColumns.size(); idx++) {

            if (idx == updatedColumns.size() - 1)
            {
                setClause += updatedColumns.get(idx) + " = " + handleApostrophe(updatedValues.get(idx));
                break;
            }

            setClause += updatedColumns.get(idx) + " = " + handleApostrophe(updatedValues.get(idx)) + ", ";
        }

        return "UPDATE " + table + setClause + " WHERE " + columnAtCondition + " = " + handleApostrophe(condition);
    }

    //endregion UPDATE queries

    //region DELETE queries

    protected final @NotNull String DELETE_ALL_RECORDS_FROM_TABLE(String table, String aColumnName) {
        return "DELETE FROM " + table + " WHERE (SELECT COUNT(" + aColumnName + ") FROM " + table + ") > 0";
    }

    //endregion DELETE queries

    private @NotNull Object handleApostrophe(@NotNull Object handledObject) {

        Class<?> objectClassType = handledObject.getClass();

        if (objectClassType == String.class) {
            return "'" + handledObject + "'";
        } else if (objectClassType == Integer.class) {
            return Integer.parseInt(handledObject.toString());
        } else {
            throw new IllegalArgumentException("The desired object unexpectedly encountered errors!  ");
        }
    }
}
