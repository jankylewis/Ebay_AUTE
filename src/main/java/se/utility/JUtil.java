package se.utility;

import com.microsoft.playwright.Page;
import org.javatuples.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.awt.Toolkit;
import java.awt.Dimension;
import java.util.*;

public class JUtil {            //J means Java language

    //region Resizing viewport

    public static class ViewportUtil {

        private int width;
        private int height;

        public void setScreenSize(@NotNull Pair<Integer, Integer> tupleOfWidthAndHeight) {
            this.width = tupleOfWidthAndHeight.getValue0();
            this.height = tupleOfWidthAndHeight.getValue1();
        }

        public Pair<Integer, Integer> getScreenSize() {
            return Pair.with(width, height);
        }

            public static @NotNull Pair<Integer, Integer> getDeviceScreenSize() {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            return Pair.with((int)dimension.getWidth(), (int)dimension.getHeight());
        }

        @Contract("_ -> param1")
        public static @NotNull Page exaggerateViewport(@NotNull Page exaggeratedPage) {

            //Forcing to generate a default tuple of screen size
            Pair<Integer, Integer> tupleOfWidthAndHeight = getDeviceScreenSize();

            //Exaggerating the Page
            exaggeratedPage.setViewportSize(
                                            tupleOfWidthAndHeight.getValue0() - 30,
                                                tupleOfWidthAndHeight.getValue1() - 145     // - 145 for taskbar
                                            );

            return exaggeratedPage;
        }

        public Page resizeViewport(@NotNull Page resizedPage,
                                   @NotNull Pair<Integer, Integer> tupleOfWidthAndHeight) {

            //Resizing the Page
            resizedPage.setViewportSize(
                                        tupleOfWidthAndHeight.getValue0(),
                                        tupleOfWidthAndHeight.getValue1()
                                        );

            return resizedPage;
        }
    }

    //endregion

    //region Thread Handler

    public static class ThreadHandler {

        public String getCurrentTestMethodName(@NotNull Class<?> currentClass) {
            String testMethodName = currentClass.getEnclosingMethod().getName();
            return testMethodName;
        }

        private void setThreadName(@NotNull Thread setThread, String threadName) {
            setThread.setName(threadName);
        }

        public Thread getCurrentThread(Thread expThread, String threadNameToSet) {

            //Setting Thread name
            setThreadName(expThread, threadNameToSet);

            return expThread;
        }
    }

    //endregion

    //region Conversion Utility

    public static class ConversionUtil {
        public static @NotNull @Unmodifiable Object convert(Object value, Class<?> parsedType) {

            Boolean isConvertedSuccessfully = false;

            //Parsing into Integer
            if (parsedType == int.class) {

                value = Integer.parseInt(value.toString());
                isConvertedSuccessfully = true;
            }

            //Parsing into String
            if (parsedType == String.class) {

                value = value.toString();
                isConvertedSuccessfully = true;
            }

            //Parsing into Double
            if (parsedType == Double.class) {

                value = Double.parseDouble(value.toString());
                isConvertedSuccessfully = true;
            }

            if (isConvertedSuccessfully) {
                return value.getClass().toGenericString();
            } else {
                throw new IllegalArgumentException("Your desired value was invalid! ");
            }
        }

        public static int identifyObjectType(Object identifiedValue) {

            if (identifiedValue instanceof Integer) {
                return 1;
            }

            return -1;
        }
    }

    //endregion

    public static class MapUtil {

        public static Object retrieveKeyFromValue(@NotNull Hashtable<?, ?> hashTable, Object value) {

            for (Map.Entry entry : hashTable.entrySet()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }

            throw new IllegalArgumentException("The expected value was not existed in the HashTable! ");
        }
        public static Object retrieveKeyFromValue(@NotNull Map<?, ?> map, Object value) {

            for (Map.Entry entry : map.entrySet()) {
                if (value.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }

            throw new IllegalArgumentException("The expected value was not existed in the Map! ");
        }

        public static List<Object> retrieveKeysFromValues(@NotNull Map<?, ?> map, Iterable<Object> values) {

            List<Object> keys = new ArrayList<>();

            for (Map.Entry entry : map.entrySet()) {
                for (Object value : values) {
                    if (value.equals(entry.getValue())) {
                        keys.add(value);
                    }
                }

                return keys;
            }

            throw new IllegalArgumentException("Several expected values were not existed in the Map! ");

        }

        //region This function needs to be modified

        public static @NotNull Map<?, ?> sortMapByInsertionOrder(
                @NotNull Map<?, ?> map) {

            List<?> values = new ArrayList<>(map.values());
            List<?> keys = new ArrayList<>(map.keySet());

            Collections.reverse(keys);
            Collections.reverse(values);

            Hashtable sortedHashTable = new Hashtable<>();
            int hashTableSize = keys.size();

            if (keys.size() != values.size()) {
                if (GlobalVariableUtil.ScriptConfiguration.TROUBLESHOOTING_MODE) {
                    throw new IllegalArgumentException(StringUtil.appendStrings(Arrays.asList(
                            "Keys' size was not equal to values' size: \n",
                            "Keys' size = ",
                            String.valueOf(keys.size()),
                            "\nValues' size = ",
                            String.valueOf(values.size())
                    )));
                }

                hashTableSize = Math.min(keys.size(), values.size());
            }

            for (int i = hashTableSize-1; i >= 0; i--) {
                sortedHashTable.put(keys.get(i), values.get(i));
            }

//            List<?> _values = new ArrayList<>(sortedHashTable.values());

            return sortedHashTable;
        }

        //endregion

        public static Object[] convertMapToArrayObject(Map<?, ?> convertedMap) {
            return null;
        }
    }

    public static class CollectionUtil {

        public static Collection<?> reverseCollectionOrder(List<?> reversedCollection) {
            Collections.reverse(reversedCollection);
            return reversedCollection;
        }

        public static List<?> reverseListOrder(List<?> reversedList) {
            Collections.reverse(reversedList);
            return reversedList;
        }
    }

    public static class ListUtil {

        public static Object @NotNull [] convertListToArrayObject(@NotNull List<?> convertedObject) {

            Object[] arrayObject = new Object[convertedObject.size()];

            for (int i = 0; i < convertedObject.size(); ++i) {
                arrayObject[i] = convertedObject.get(i);
            }

            return arrayObject;
        }
    }
}