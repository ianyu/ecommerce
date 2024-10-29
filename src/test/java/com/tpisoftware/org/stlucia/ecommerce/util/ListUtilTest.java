package com.tpisoftware.org.stlucia.ecommerce.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListUtilTest {

    private final static String INPUT_PARAMETER_MUST_NOT_BE_NULL = "The input parameter must not be null.";
    private static final String INSTANCE_OF_ERROR_MESSAGE = "The provided object is not an instance of: %s.";

    @Test
    void testIsElementInstanceOf_NullList_ThrowsException() {
        List<Object> objs = null;

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> ListUtil.isElementInstanceOf(objs, ClassB.class));

        assertEquals(INPUT_PARAMETER_MUST_NOT_BE_NULL, exception.getMessage());
    }

    @Test
    void testIsElementInstanceOf_EmptyList_ReturnsTrue() {
        List<Object> objs = new ArrayList<>();

        boolean resultA = ListUtil.isElementInstanceOf(objs, ClassA.class);
        boolean resultB = ListUtil.isElementInstanceOf(objs, ClassB.class);

        assertTrue(resultA);
        assertTrue(resultB);
    }

    @Test
    void testIsElementInstanceOf_ListOfDifferentObject_ReturnFalse() {
        List<ClassA> objs = new ArrayList<>();
        objs.add(new ClassA());

        boolean result = ListUtil.isElementInstanceOf(objs, ClassB.class);

        assertFalse(result);
    }

    @Test
    void testIsElementInstanceOf_ListOfSameObject_ReturnTrue() {
        List<ClassB> objs = new ArrayList<>();
        objs.add(new ClassB());

        boolean result = ListUtil.isElementInstanceOf(objs, ClassB.class);

        assertTrue(result);
    }

    @Test
    void testConvertTo_NullList_ReturnNull() {
        List<ClassB> result = ListUtil.convertTo(null, ClassB.class);
        assertNull(result);
    }

    @Test
    void testConvertTo_EmptyList_ReturnsEmptyList() {
        List<Object> objs = new ArrayList<>();

        List<ClassA> resultA = ListUtil.convertTo(objs, ClassA.class);
        List<ClassB> resultB = ListUtil.convertTo(objs, ClassB.class);

        assertTrue(resultA.isEmpty());
        assertTrue(resultB.isEmpty());
    }

    @Test
    void testConvertTo_ListOfDifferentObject_ThrowsException() {
        String expected = String.format(INSTANCE_OF_ERROR_MESSAGE, ClassB.class.getName());

        List<ClassA> objs = new ArrayList<>();
        objs.add(new ClassA());

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> ListUtil.convertTo(objs, ClassB.class));

        assertEquals(expected, exception.getMessage());
    }

    @Test
    void testConvertTo_ListOfSameObject_ReturnList() {
        List<ClassB> expected = new ArrayList<>();
        expected.add(new ClassB());

        List<ClassB> objs = new ArrayList<>();
        objs.add(new ClassB());

        List<ClassB> result = ListUtil.convertTo(objs, ClassB.class);

        assertEquals(expected, result);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ClassA {
        private Long id;
        private String aname;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ClassB {
        private Long id;
        private String bname;
    }
}