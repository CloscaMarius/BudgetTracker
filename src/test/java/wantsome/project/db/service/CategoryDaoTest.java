package wantsome.project.db.service;

import org.junit.*;
import wantsome.project.db.DbManager;
import wantsome.project.db.dto.CategoryDto;
import wantsome.project.db.dto.Type;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CategoryDaoTest {
    private static final String TEST_DB_FILE = "budget_tracker_testCategories.db";

    private static final List<CategoryDto> sampleItems = Arrays.asList(
            new CategoryDto("rent", Type.EXPENSE),
            new CategoryDto("salary", Type.INCOME),
            new CategoryDto("bills", Type.EXPENSE));

    private final CategoryDao dao = new CategoryDao();

    @BeforeClass
    public static void initDbBeforeAnyTests() {
        DbManager.setDbFile(TEST_DB_FILE); //use a separate db for test, to avoid overwriting the real one
        DbInitService.createMissingTables();
    }

    @Before
    public void insertRowsBeforeTest() {
        assertTrue(dao.getAll().isEmpty());
        for (CategoryDto item : sampleItems) {
            dao.insert(item);
        }
    }

    @After
    public void deleteRowsAfterTest() {
        for (CategoryDto item : dao.getAll()) {
            dao.delete(item.getId());
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
        CategoryDto item1fromDb = dao.getAll().get(0);

        assertEquals(item1fromDb, dao.getById(item1fromDb.getId()).get());
    }


    @Test
    public void get_forInvalidId() {
        assertFalse(dao.getById(-99).isPresent());
    }

    @Test
    public void insert() {
        assertEquals(3, dao.getAll().size());

        CategoryDto newItem = new CategoryDto("sales", Type.INCOME);
        dao.insert(newItem);

        assertEquals(4, dao.getAll().size());
    }

    private void checkOnlyTheSampleItemsArePresentInDb() {
        List<CategoryDto> itemsFromDb = dao.getAll();
        assertEquals(3, itemsFromDb.size());

    }

    @Test
    public void delete() {
        List<CategoryDto> itemsFromDb = dao.getAll();
        dao.delete(itemsFromDb.get(0).getId());

        List<CategoryDto> itemsInDBAfterDelete = dao.getAll();
        assertEquals(2, itemsInDBAfterDelete.size());

    }

    @Test
    public void delete_forInvalidId() {
        checkOnlyTheSampleItemsArePresentInDb();
        dao.delete(-66);
        checkOnlyTheSampleItemsArePresentInDb();
    }
}
