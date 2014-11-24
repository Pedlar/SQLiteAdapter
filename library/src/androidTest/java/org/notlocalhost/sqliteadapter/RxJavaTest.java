package org.notlocalhost.sqliteadapter;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by pedlar on 11/23/14.
 */
public class RxJavaTest extends ApplicationTestCase<Application> {
    public RxJavaTest() {
        super(Application.class);
    }
    final String databaseName = "test_db" + new Random(13233324).nextLong() + ".db";
    SQLiteAdapter adapter;

    @Mock
    Observer<Object> subscriber;

    @Override
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        createApplication();
        adapter = new SQLiteAdapter.Builder(getContext())
                .setSchemas(TestTable.class)
                .setDatabaseName(databaseName)
                .build();
    }

    @Override
    public void tearDown() throws Exception {
        Context context = getContext().getApplicationContext();
        if(context != null) {
            context.deleteDatabase(databaseName);
        }
        super.tearDown();
    }

    public void testAdapterInsertQueryObservable() {
        TestInterface testInterface = adapter.create(TestInterface.class);
        final String col1 = "TestData1";
        final int testInt = 42;
        final long testLong = 424242424242l;
        testInterface.setTestData(col1, testInt, testLong);

        testInterface.getTestDataObservable(col1).subscribe(subscriber);
        verify(subscriber, times(1)).onNext(any());
    }

    public void testAdapterQueryListObservable() {
        String col1 = "TestData1";
        TestInterface testInterface = adapter.create(TestInterface.class);
        testInterface.setTestData(col1, 42, 42l);
        testInterface.setTestData(col1, 43, 43l);
        testInterface.setTestData(col1, 44, 44l);

        testInterface.getTestDataObservableList(col1).subscribe(subscriber);
        verify(subscriber, times(1)).onNext(any());

        testInterface.getTestDataObservableList(col1)
                .flatMap(new Func1<ArrayList<TestTable>, Observable<TestTable>>() {
                    @Override
                    public Observable<TestTable> call(ArrayList<TestTable> testTable) {
                        return Observable.from(testTable);
                    }
                }).subscribe(subscriber);
        verify(subscriber, times(4)).onNext(any()); // 3 for the flatMap, and one for the List onNext()
    }

}
