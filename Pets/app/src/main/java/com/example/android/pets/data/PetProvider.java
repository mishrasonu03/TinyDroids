package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    private PetDbHelper mDbHelper;

    private static final int PETS = 100;

    private static final int PET_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PETS);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues contentValues) {

        String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if(name == null){
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if(weight != null && weight < 0){
            throw new IllegalArgumentException("Pet requires a valid weight");
        }

        Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if(gender == null || !PetContract.PetEntry.isValidGender(gender)){
            throw new IllegalArgumentException("Pet requires a valid gender");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);
        if (newRowId == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, newRowId);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)){
            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if(name == null){
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)){
            Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if(weight != null && weight < 0){
                throw new IllegalArgumentException("Pet requires a valid weight");
            }
        }

        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)){
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if(gender == null || !PetContract.PetEntry.isValidGender(gender)){
                throw new IllegalArgumentException("Pet requires a valid gender");
            }
        }

        if(contentValues.size() == 0){
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}