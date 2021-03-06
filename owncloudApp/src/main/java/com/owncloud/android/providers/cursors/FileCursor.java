/**
 * ownCloud Android client application
 *
 * @author Bartosz Przybylski
 * Copyright (C) 2015  Bartosz Przybylski
 * Copyright (C) 2016 ownCloud GmbH.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.providers.cursors;

import android.annotation.TargetApi;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.DocumentsContract.Document;

import com.owncloud.android.datamodel.OCFile;
import com.owncloud.android.utils.MimetypeIconUtil;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class FileCursor extends MatrixCursor {

    private static final String[] DEFAULT_DOCUMENT_PROJECTION = new String[]{
            Document.COLUMN_DOCUMENT_ID, Document.COLUMN_DISPLAY_NAME,
            Document.COLUMN_MIME_TYPE, Document.COLUMN_SIZE,
            Document.COLUMN_FLAGS, Document.COLUMN_LAST_MODIFIED
    };

    private Bundle mExtras = Bundle.EMPTY;

    public FileCursor(String[] projection) {
        super(projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION);
    }

    public void addFile(OCFile file) {
        if (file == null) {
            return;
        }

        final int iconRes = MimetypeIconUtil.getFileTypeIconId(file.getMimetype(), file.getFileName());
        final String mimeType = file.isFolder() ? Document.MIME_TYPE_DIR : file.getMimetype();
        final String imagePath = file.isImage() && file.isDown() ? file.getStoragePath() : null;
        int flags = imagePath != null ? Document.FLAG_SUPPORTS_THUMBNAIL : 0;

        newRow().add(Document.COLUMN_DOCUMENT_ID, Long.toString(file.getFileId()))
                .add(Document.COLUMN_DISPLAY_NAME, file.getFileName())
                .add(Document.COLUMN_LAST_MODIFIED, file.getModificationTimestamp())
                .add(Document.COLUMN_SIZE, file.getFileLength())
                .add(Document.COLUMN_FLAGS, flags)
                .add(Document.COLUMN_ICON, iconRes)
                .add(Document.COLUMN_MIME_TYPE, mimeType);
    }

    public void setMoreToSync(boolean hasMoreToSync) {
        mExtras = new Bundle();
        mExtras.putBoolean(DocumentsContract.EXTRA_LOADING, hasMoreToSync);
    }

    @Override
    public Bundle getExtras() {
        return mExtras;
    }
}
