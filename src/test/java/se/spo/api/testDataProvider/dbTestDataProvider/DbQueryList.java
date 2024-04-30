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

    //endregion UPDATE queries

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
