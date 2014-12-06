/* 
 * Copyright (c) 2008-2010, Hazel Ltd. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hazelcast.query;

import com.hazelcast.core.MapEntry;
import org.junit.Test;

import static org.junit.Assert.*;

public class PredicatesTest {
    @Test
    public void testEqual() {
        Object value = new QueryTest.Employee("abc-123-xvz", 34, true, 10D);
        assertTrue(new SqlPredicate("(age >= " + 20 + ") AND (age <= " + 40 + ")").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("(age >= " + 20 + ") AND (age <= " + 34 + ")").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("(age >= " + 34 + ") AND (age <= " + 35 + ")").apply(createEntry("1", value)));
//        assertTrue(new SqlPredicate(" (name LIKE 'abc-%') AND (age <= " + 40 + ")").apply(createEntry("1", value)));
        assertFalse(new SqlPredicate("age = 33").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("age = 34").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("age > 5").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("salary > 5").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("salary > 5 and salary < 11").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("salary between 9.99 and 10.01").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("salary between 5 and 15").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("name='abc-123-xvz'").apply(createEntry("1", value)));
        assertTrue(new SqlPredicate("name='abc 123-xvz'").apply(createEntry("1", new QueryTest.Employee("abc 123-xvz", 34, true, 10D))));
        assertTrue(new SqlPredicate("name='abc 123-xvz+(123)'").apply(createEntry("1", new QueryTest.Employee("abc 123-xvz+(123)", 34, true, 10D))));
        assertFalse(new SqlPredicate("name='abc 123-xvz+(123)'").apply(createEntry("1", new QueryTest.Employee("abc123-xvz+(123)", 34, true, 10D))));
        assertTrue(new SqlPredicate("name LIKE 'abc-%'").apply(createEntry("1", new QueryTest.Employee("abc-123", 34, true, 10D))));
        assertTrue(Predicates.equal(new DummyExpression("value"), "value").apply(null));
        assertFalse(Predicates.equal(new DummyExpression("value1"), "value").apply(null));
        assertFalse(Predicates.equal(new DummyExpression("value"), "value1").apply(null));
        assertTrue(Predicates.equal(new DummyExpression(true), Boolean.TRUE).apply(null));
        assertTrue(Predicates.equal(new DummyExpression(Boolean.TRUE), true).apply(null));
        assertFalse(Predicates.equal(new DummyExpression(Boolean.FALSE), true).apply(null));
        assertFalse(Predicates.equal(new DummyExpression(15.23), 15.22).apply(null));
        assertFalse(Predicates.equal(new DummyExpression(15.23), 15.22).apply(null));
        assertTrue(Predicates.equal(new DummyExpression(15.22), 15.22).apply(null));
        assertFalse(Predicates.equal(new DummyExpression(15), 16).apply(null));
        assertTrue(Predicates.greaterThan(new DummyExpression(6), 5).apply(null));
        assertFalse(Predicates.greaterThan(new DummyExpression(4), 5).apply(null));
        assertFalse(Predicates.greaterThan(new DummyExpression(5), 5).apply(null));
        assertTrue(Predicates.greaterEqual(new DummyExpression(5), 5).apply(null));
        assertTrue(Predicates.lessThan(new DummyExpression(6), 7).apply(null));
        assertFalse(Predicates.lessThan(new DummyExpression(4), 3).apply(null));
        assertFalse(Predicates.lessThan(new DummyExpression(4), 4).apply(null));
        assertTrue(Predicates.lessEqual(new DummyExpression(4), 4).apply(null));
        assertTrue(Predicates.between(new DummyExpression(5), 4, 6).apply(null));
        assertTrue(Predicates.between(new DummyExpression(5), 5, 6).apply(null));
        assertFalse(Predicates.between(new DummyExpression(5), 6, 7).apply(null));
        assertTrue(Predicates.in(new DummyExpression(5), 4, 7, 8, 5).apply(null));
        assertTrue(Predicates.in(new DummyExpression(5), 5, 7, 8).apply(null));
        assertFalse(Predicates.in(new DummyExpression(5), 6, 7, 8).apply(null));
        assertFalse(Predicates.in(new DummyExpression(9), 6, 7, 8).apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "J%").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "Ja%").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "J_v_").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "_av_").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "_a__").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "J%v_").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java"), "J%_").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "java").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "j%").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "J_a").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "J_ava").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "J_a_a").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "J_av__").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java"), "J_Va").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java World"), "Java World").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java World"), "Java%ld").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java World"), "%World").apply(null));
        assertTrue(Predicates.like(new DummyExpression<String>("Java World"), "Java_World").apply(null));
        assertFalse(Predicates.like(new DummyExpression<String>("Java World"), "JavaWorld").apply(null));
    }

    @Test
    public void testCriteriaAPI() {
        Object value = new QueryTest.Employee("abc-123-xvz", 34, true, 10D);
        EntryObject e = new PredicateBuilder().getEntryObject();
        EntryObject e2 = e.get("age");
        Predicate predicate = e2.greaterEqual(29).and(e2.lessEqual(36));
        assertTrue(predicate.apply(createEntry("1", value)));
    }

    @Test
    public void testSqlPredicate() {
        assertEquals("(name LIKE 'joe' AND id=5)", sql("name like 'joe' AND id = 5"));
        assertEquals("active=true", sql("active"));
        assertEquals("(active=true AND name=abc xyz 123)", sql("active AND name='abc xyz 123'"));
        assertEquals("(name LIKE 'abc-xyz+(123)' AND name=abc xyz 123)", sql("name like 'abc-xyz+(123)' AND name='abc xyz 123'"));
        assertEquals("(active=true AND age>4)", sql("active and age > 4"));
        assertEquals("(active=true AND age>4)", sql("active and age>4"));
        assertEquals("(active=false AND age<=4)", sql("active=false AND age<=4"));
        assertEquals("(active=false AND age<=4)", sql("active= false and age <= 4"));
        assertEquals("(active=false AND age>=4)", sql("active=false AND (age>=4)"));
        assertEquals("(active=false OR age>=4)", sql("active =false or (age>= 4)"));
        assertEquals("name LIKE 'J%'", sql("name like 'J%'"));
        assertEquals("NOT(name LIKE 'J%')", sql("name not like 'J%'"));
        assertEquals("(active=false OR name LIKE 'J%')", sql("active =false or name like 'J%'"));
        assertEquals("(active=false OR name LIKE 'Java World')", sql("active =false or name like 'Java World'"));
        assertEquals("(active=false OR name LIKE 'Java W% Again')", sql("active =false or name like 'Java W% Again'"));
        assertEquals("age IN (10,15)", sql("age in (10, 15)"));
        assertEquals("NOT(age IN (10,15))", sql("age not in ( 10 , 15 )"));
        assertEquals("(active=true AND age BETWEEN 10 AND 15)", sql("active and age between 10 and 15"));
        assertEquals("(age IN (10,15) AND active=true)", sql("age IN (10, 15) and active"));
        assertEquals("(active=true OR age IN (10,15))", sql("active or (age in ( 10,15))"));
        assertEquals("(age>10 AND (active=true OR age IN (10,15)))", sql("age>10 AND (active or (age IN (10, 15 )))"));
        assertEquals("(age<=10 AND (active=true OR NOT(age IN (10,15))))", sql("age<=10 AND (active or (age not in (10 , 15)))"));
        assertEquals("age BETWEEN 10 AND 15", sql("age between 10 and 15"));
        assertEquals("NOT(age BETWEEN 10 AND 15)", sql("age not between 10 and 15"));
        assertEquals("(active=true AND age BETWEEN 10 AND 15)", sql("active and age between 10 and 15"));
        assertEquals("(age BETWEEN 10 AND 15 AND active=true)", sql("age between 10 and 15 and active"));
        assertEquals("(active=true OR age BETWEEN 10 AND 15)", sql("active or (age between 10 and 15)"));
        assertEquals("(age>10 AND (active=true OR age BETWEEN 10 AND 15))", sql("age>10 AND (active or (age between 10 and 15))"));
        assertEquals("(age<=10 AND (active=true OR NOT(age BETWEEN 10 AND 15)))", sql("age<=10 AND (active or (age not between 10 and 15))"));
    }

    private String sql(String sql) {
        return new SqlPredicate(sql).toString();
    }

    class DummyExpression<T> implements Expression<T> {
        T value;

        DummyExpression(T value) {
            this.value = value;
        }

        public T getValue(Object obj) {
            return value;
        }
    }

    static MapEntry createEntry(final Object key, final Object value) {
        return new MapEntry() {
            public long getCost() {
                return 0;
            }

            public long getCreationTime() {
                return 0;
            }

            public long getExpirationTime() {
                return 0;
            }

            public int getHits() {
                return 0;
            }

            public long getLastAccessTime() {
                return 0;
            }

            public long getLastUpdateTime() {
                return 0;
            }

            public long getVersion() {
                return 0;
            }

            public boolean isValid() {
                return true;
            }

            public Object getKey() {
                return key;
            }

            public Object getValue() {
                return value;
            }

            public Object setValue(Object value) {
                return value;
            }
        };
    }
}
