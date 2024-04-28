package se.spo.api.testDataProvider.dbTestDataProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DbQueryList {

    protected DbQueryList(){}

    //region SELECT queries

    protected final String SELECT_ALL = "SELECT * FROM userAuthenticationTb";
    protected final String SELECT_RECORD_1UP = "SELECT clientId FROM userAuthenticationTb WHERE (SELECT COUNT(clientId) FROM userAuthenticationTb) > 0";

    //endregion SELECT queries

    //region INSERT queries

    protected final @NotNull String INSERT_VALUES(String tableName, @NotNull List<String> tableColumns, List<Object> tableData){

        String tableColumnsString = "(";
        String tableDataString = "(";

        for (int idx = 0; idx < tableColumns.size(); idx++){

            if (idx == tableColumns.size() - 1) {
                tableColumnsString += tableColumns.get(idx) + ") ";
                break;
            }

            tableColumnsString += tableColumns.get(idx) + ", ";
        }

        for (int idx = 0; idx < tableData.size(); idx++){

            if (idx == tableData.size() - 1) {
                tableDataString += handleApostrophe(tableData.get(idx)) + ")";
                break;
            }

            tableDataString += handleApostrophe(tableData.get(idx)) + ", ";
        }

        return "INSERT INTO " + tableName + tableColumnsString + "VALUES "+ tableDataString;
    }

    //endregion INSERT queries

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
