package me.eneas.moviespollcontentprovider;

import android.provider.BaseColumns;

/**
 * Created by a67281303 on 8/11/16.
 */

public interface MoviesPollContract extends BaseColumns {

    String AUTHORITY = "me.eneas.moviespollcontentprovider";

    interface Movie {
        String TABLE_NAME = "movies";
        String COL_ID_MOVIE = _ID;
        String COL_TITLE = "title";
        String COL_YEAR = "year";
        String COL_POPULARITY = "popularity";
    }

    interface Poll {
        String TABLE_NAME = "poll";
        String COL_ID_POLL = _ID;
        String COL_ID_MOVIE_FK = "id_movie_fk";
        String COL_VALUE = "value";
    }


}
