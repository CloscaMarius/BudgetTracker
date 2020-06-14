package wantsome.project.db.service;

import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.TransactionDto;
import wantsome.project.ui.web.TransactionStats;

import java.io.File;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionDaoTest {

    private static final String TEST_DB_FILE = "src/main/resources/budget_tracker_test.db";


    private static final List<TransactionDto> sampleTransactions = Arrays.asList(
            new TransactionDto(1, Date.valueOf("2018-05-02"), "rent payed", 150.47),
            new TransactionDto(2, Date.valueOf("2018-05-01"), "salary", 500.0),
            new TransactionDto(3, Date.valueOf("2018-05-03"), "bill payed", 75.83));


    private final TransactionDao dao = new TransactionDao();


    @BeforeClass
    public static void initDbBeforeAnyTests() {
        DbManager.setDbFile(TEST_DB_FILE); //use a separate db for test, to avoid overwriting the real one
        DbInitService.createTablesAndInitialData();
    }


    @Before
    public void insertTransactionsRowsBeforeTest() {
        assertTrue(dao.getAll().isEmpty());


        for (TransactionDto item : sampleTransactions) {
            dao.insert(item);
        }
    }

    @After
    public void deleteTransactionsRowsAfterTest() {
        for (TransactionDto item : dao.getAll()) {
            dao.deleteById(item.getId());
        }
        assertTrue(dao.getAll().isEmpty());
    }

    @AfterClass
    public static void deleteDbFileAfterAllTests() {
        new File(TEST_DB_FILE).delete();
    }


    @Test
    public void getAll() {
        checkOnlyTheSampleItemsArePresentInDb();
    }


    @Test
    public void get() {
        TransactionDto item1fromDb = dao.getAll().get(0);

        assertEquals(item1fromDb, dao.getById(item1fromDb.getId()).get());
    }

    @Test
    public void getByDate() {
        List<TransactionDto> itemsFromDb = dao.getByDate("2018-05-03");
        assertEquals(1, itemsFromDb.size());
        assertEquals("2018-05-03", itemsFromDb.get(0).getDate().toString());

    }

    @Test
    public void getByDateInterval() {
        List<TransactionDto> itemsFromDb = dao.getByDateInterval(Date.valueOf("2018-05-02"), Date.valueOf("2018-05-04"));
        assertEquals(2, itemsFromDb.size());
        assertEquals("2018-05-02", itemsFromDb.get(0).getDate().toString());
        assertEquals("2018-05-03", itemsFromDb.get(1).getDate().toString());
    }

    @Test
    public void getByAmount() {
        List<TransactionDto> itemsFromDb = dao.getByAmount(500);
        assertEquals(1, itemsFromDb.size());
        assertEquals(500.0, itemsFromDb.get(0).getAmount(), 0);
    }

    @Test
    public void get_forInvalidId() {
        assertFalse(dao.getById(-99).isPresent());
    }

    @Test
    public void insert() {
        assertEquals(3, dao.getAll().size());

        TransactionDto newItem = new TransactionDto(4, Date.valueOf("2018-05-05"), "sales", 34.8);
        dao.insert(newItem);

        assertEquals(4, dao.getAll().size());
    }

    private void checkOnlyTheSampleItemsArePresentInDb() {
        List<TransactionDto> itemsFromDb = dao.getAll();
        assertEquals(3, itemsFromDb.size());

    }

    @Test
    public void delete_forInvalidId() {
        checkOnlyTheSampleItemsArePresentInDb();
        dao.deleteById(-66);
        checkOnlyTheSampleItemsArePresentInDb();
    }


    @Test
    public void getIncomeByDateInterval() {
        List<TransactionDto> itemsFromDb = dao.getIncomeByDateInterval(Date.valueOf("2018-04-25"), Date.valueOf("2018-05-03"));
        assertEquals(1, itemsFromDb.size());
        assertEquals("2018-05-01", itemsFromDb.get(0).getDate().toString());
        assertEquals(500, itemsFromDb.get(0).getAmount(), 0);
    }

    @Test
    public void getAllIncome() {
        List<TransactionDto> itemsFromDb = dao.getAllIncome();
        assertEquals(1, itemsFromDb.size());
        assertEquals("2018-05-01", itemsFromDb.get(0).getDate().toString());
        assertEquals(500, itemsFromDb.get(0).getAmount(), 0);
    }

    @Test
    public void getExpensesByDateInterval() {
        List<TransactionDto> itemsFromDb = dao.getExpensesByDateInterval(Date.valueOf("2018-04-25"), Date.valueOf("2018-05-02"));
        assertEquals(1, itemsFromDb.size());
        assertEquals("2018-05-02", itemsFromDb.get(0).getDate().toString());
        assertEquals(150.47, itemsFromDb.get(0).getAmount(), 0);
    }

    @Test
    public void getAllExpenses() {
        List<TransactionDto> itemsFromDb = dao.getAllExpenses();
        assertEquals(2, itemsFromDb.size());
        assertEquals("2018-05-02", itemsFromDb.get(0).getDate().toString());
        assertEquals(150.47, itemsFromDb.get(0).getAmount(), 0);
    }

    @Test
    public void incomeByDateInterval() {
        double itemsFromDb = TransactionStats.incomeByDateInterval(Date.valueOf("2018-05-01"), Date.valueOf("2018-05-02"));
        assertEquals(500, itemsFromDb, 0);
    }

    @Test
    public void expenseByDateInterval() {
        double itemsFromDb = TransactionStats.expensesByDateInterval(Date.valueOf("2018-05-01"), Date.valueOf("2018-05-05"));
        assertEquals(226.3, itemsFromDb, 0);

        double itemsFromDb2 = TransactionStats.expensesByDateInterval(Date.valueOf("2018-05-03"), Date.valueOf("2018-05-05"));
        assertEquals(75.83, itemsFromDb2, 0);

    }

    @Test
    public void allIncome() {
        double itemsFromDb = TransactionStats.allIncome();
        assertEquals(500, itemsFromDb, 0);
    }

    @Test
    public void allExpenses() {
        double itemsFromDb = TransactionStats.allExpenses();
        assertEquals(226.3, itemsFromDb, 0);
    }

    @Test
    public void getBalanceByDateInterval() {
        double itemsFromDb = TransactionStats.balanceByDateInterval(Date.valueOf("2018-05-01"), Date.valueOf("2018-05-02"));
        assertEquals(349.53, itemsFromDb, 0);
    }

    @Test
    public void getBalance() {
        double itemsFromDb = TransactionStats.balance();
        assertEquals(273.7, itemsFromDb, 0);
    }


}
