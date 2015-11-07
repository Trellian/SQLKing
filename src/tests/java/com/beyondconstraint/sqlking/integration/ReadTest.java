package com.beyondconstraint.sqlking.integration;

import com.beyondconstraint.sqlking.integration.models.User;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;
import com.beyondconstraint.sqlking.operation.clause.And;
import com.beyondconstraint.sqlking.operation.clause.In;
import com.beyondconstraint.sqlking.operation.clause.Or;
import com.beyondconstraint.sqlking.operation.function.Select;
import com.beyondconstraint.sqlking.operation.clause.Where;
import com.beyondconstraint.sqlking.operation.keyword.OrderBy;

import org.junit.After;
import org.junit.Before;

public class ReadTest extends IntegrationTest {

    @Before
    public void setUp() {
        getSetupUser().tearDownFourTestUsers(getSQLProvider());
        getSetupUser().setupFourTestUsers(getSQLProvider());
    }

    @org.junit.Test
    public void testAllUsersAreSelected() {
        User[] users = Select.getBuilder().execute(User.class, getSQLProvider());

        // 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testEqualToSingleSelection() {
        User user = Select.getBuilder()
                .where(new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME))
                .executeSingle(User.class, getSQLProvider());

        assertEquals(SetupUser.CLYDE_USER_NAME, user.getUsername());
    }

    @org.junit.Test
    public void testEqualToBooleanSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("isRegistered", Where.Exp.EQUAL_TO, true))
                .execute(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testEqualToLongSelection() {
        User user = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .executeSingle(User.class, getSQLProvider());

        assertEquals(SetupUser.CLYDE_USER_NAME, user.getUsername());
        assertEquals(SetupUser.CLYDE_TIMESTAMP, user.getTimestamp());
        assertEquals(SetupUser.CLYDE_IS_REGISTERED, user.getIsRegistered());
    }

    @org.junit.Test
    public void testMoreThanSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.MORE_THAN, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testMoreThanOrEqualToSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.MORE_THAN_OR_EQUAL_TO, SetupUser.CLYDE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // All 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testLessThanSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.LESS_THAN, SetupUser.ANGIE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testLessThanOrEqualToSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("timestamp", Where.Exp.LESS_THAN_OR_EQUAL_TO, SetupUser.ANGIE_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 4 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 4 rows will be selected
        assertEquals(4, users.length);
    }

    @org.junit.Test
    public void testLikeStartingWithSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("username", Where.Exp.LIKE, "jo%"))
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testLikeEndingWithSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("username", Where.Exp.LIKE, "%e"))
                .execute(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testLikeContainingSelection() {
        User[] users = Select.getBuilder()
                .where(new Where("username", Where.Exp.LIKE, "%lyd%"))
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testInStringSelection() {
        User[] users = Select.getBuilder()
                .where(new In("username", SetupUser.CLYDE_USER_NAME, SetupUser.ANGIE_USER_NAME))
                .execute(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testInLongSelection() {
        User[] users = Select.getBuilder()
                .where(new In("timestamp", SetupUser.CLYDE_TIMESTAMP, SetupUser.ANGIE_TIMESTAMP, SetupUser.GILL_TIMESTAMP))
                .execute(User.class, getSQLProvider());

        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testOrWhereInQueryIsBuiltFromClause() {
        User[] users = Select.getBuilder()
                .where(new Or(
                        new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                        new In("timestamp", SetupUser.GILL_TIMESTAMP, SetupUser.ANGIE_TIMESTAMP)
                ))
                .execute(User.class, getSQLProvider());


        // 3 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 3 rows will be selected
        assertEquals(3, users.length);
    }

    @org.junit.Test
    public void testAndEqualOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(new And(
                        new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                        new Where("isRegistered", Where.Exp.EQUAL_TO, SetupUser.CLYDE_IS_REGISTERED),
                        new Where("timestamp", Where.Exp.EQUAL_TO, SetupUser.CLYDE_TIMESTAMP)
                ))
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testOrEqualOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(new Or(
                        new Where("username",Where.Exp.EQUAL_TO,SetupUser.CLYDE_USER_NAME),
                        new Where("username",Where.Exp.EQUAL_TO,SetupUser.ANGIE_USER_NAME)
                ))
                .execute(User.class, getSQLProvider());

        // 2 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 2 rows will be selected
        assertEquals(2, users.length);
    }

    @org.junit.Test
    public void testAndOrEqualsOperationsSelection() {
        User[] users = Select.getBuilder()
                .where(
                        new And(
                                new Or(
                                        new Where("username", Where.Exp.EQUAL_TO, SetupUser.CLYDE_USER_NAME),
                                        new Where("username", Where.Exp.EQUAL_TO, SetupUser.ANGIE_USER_NAME)
                                ),
                                new And(
                                        new Where("timestamp", Where.Exp.MORE_THAN_OR_EQUAL_TO, SetupUser.ANGIE_TIMESTAMP)
                                )
                        )
                )
                .execute(User.class, getSQLProvider());

        // 1 of the users created by #setupFourTestUsers will match the
        // exercise clause, therefore, we assert that 1 rows will be selected
        assertEquals(1, users.length);
    }

    @org.junit.Test
    public void testNumericOrderByAscSelection() {
        User[] users = Select.getBuilder()
                .orderBy("timestamp", OrderBy.Order.ASC)
                .execute(User.class, getSQLProvider());

        // clyde, gill, josh, angie is the timestamp ascending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.CLYDE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testNumericOrderByDescSelection() {
        User[] users = Select.getBuilder()
                .orderBy("timestamp", OrderBy.Order.DESC)
                .execute(User.class, getSQLProvider());

        // angie, josh, gill, clyde is the timestamp descending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.ANGIE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testAlphaOrderByAscSelection() {
        User[] users = Select.getBuilder()
                .orderBy("username", OrderBy.Order.ASC)
                .execute(User.class, getSQLProvider());

        // angie, clyde, gill, josh is the username ascending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.ANGIE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.JOSH_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testAlphaOrderByDescSelection() {
        User[] users = Select.getBuilder()
                .orderBy("username", OrderBy.Order.DESC)
                .execute(User.class, getSQLProvider());

        // josh, gill, clyde, angie is the username descending order of the users created
        // by #setupFourTestUsers, therefore, we assert that the rows will be
        // selected in this order
        assertEquals(4, users.length);
        assertEquals(SetupUser.JOSH_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
        assertEquals(SetupUser.CLYDE_USER_NAME, users[2].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[3].getUsername());
    }

    @org.junit.Test
    public void testLimitLowerBoundSelection() {
        User[] users = Select.getBuilder()
                .limit(0, 2)
                .orderBy("username", OrderBy.Order.DESC)
                .execute(User.class, getSQLProvider());

        assertEquals(2, users.length);
        assertEquals(SetupUser.JOSH_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.GILL_USER_NAME, users[1].getUsername());
    }

    @org.junit.Test
    public void testLimitUpperBoundSelection() {
        User[] users = Select.getBuilder()
                .limit(2,4)
                .orderBy("username", OrderBy.Order.DESC)
                .execute(User.class, getSQLProvider());

        assertEquals(2, users.length);
        assertEquals(SetupUser.CLYDE_USER_NAME, users[0].getUsername());
        assertEquals(SetupUser.ANGIE_USER_NAME, users[1].getUsername());
    }
}