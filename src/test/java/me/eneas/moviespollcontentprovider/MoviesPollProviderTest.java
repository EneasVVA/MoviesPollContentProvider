package me.eneas.moviespollcontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MoviesPollProviderTest extends ProviderTestCase2<MoviesPollProvider> {

    public MoviesPollProviderTest() {
        super(MoviesPollProvider.class, MoviesPollContract.AUTHORITY);
    }

    @Test
    public void testInsert() {
        ContentValues values = new ContentValues();
        values.put(MoviesPollContract.Movie.COL_TITLE, "Gameover");
        values.put(MoviesPollContract.Movie.COL_YEAR, 2014);
        values.put(MoviesPollContract.Movie.COL_POPULARITY, 1.0007);

        Uri uri = getProvider().insert(MoviesPollProvider.CONTENT_URI, values);
        assertNotNull(uri);
    }

    @Test
    public void testQuery1() {
        Cursor c = getProvider().query(MoviesPollProvider.CONTENT_URI, null, null, null, null);
        assertFalse(c.moveToFirst());
        assertTrue(c.getColumnIndex(MoviesPollContract.Movie.COL_ID_MOVIE) >= 0);
        c.close();

    }
}