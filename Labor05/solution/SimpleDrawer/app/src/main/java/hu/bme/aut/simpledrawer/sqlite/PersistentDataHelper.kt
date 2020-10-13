package hu.bme.aut.simpledrawer.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import hu.bme.aut.simpledrawer.model.Line
import hu.bme.aut.simpledrawer.model.Point

class PersistentDataHelper(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: DbHelper = DbHelper(context)

    private val pointColumns = arrayOf<String>(
        DbConstants.Points.Columns._id.name,
        DbConstants.Points.Columns.coord_x.name,
        DbConstants.Points.Columns.coord_y.name
    )

    private val lineColumns = arrayOf<String>(
        DbConstants.Lines.Columns._id.name,
        DbConstants.Lines.Columns.start_x.name,
        DbConstants.Lines.Columns.start_y.name,
        DbConstants.Lines.Columns.end_x.name,
        DbConstants.Lines.Columns.end_y.name

    )

    @Throws(SQLiteException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun persistPoints(points: List<Point>) {
        clearPoints()
        for (point in points) {
            val values = ContentValues()
            values.put(DbConstants.Points.Columns.coord_x.name, point.x)
            values.put(DbConstants.Points.Columns.coord_y.name, point.y)
            database!!.insert(DbConstants.Points.DATABASE_TABLE, null, values)
        }
    }

    fun restorePoints(): MutableList<Point> {
        val points: MutableList<Point> = ArrayList()
        val cursor: Cursor =
            database!!.query(DbConstants.Points.DATABASE_TABLE, pointColumns, null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            val point: Point = cursorToPoint(cursor)
            points.add(point)
            cursor.moveToNext()
        }
        cursor.close()
        return points
    }

    fun clearPoints() {
        database!!.delete(DbConstants.Points.DATABASE_TABLE, null, null)
    }

    private fun cursorToPoint(cursor: Cursor): Point {
        val point = Point()
        point.x =cursor.getFloat(DbConstants.Points.Columns.coord_x.ordinal)
        point.y =cursor.getFloat(DbConstants.Points.Columns.coord_y.ordinal)
        return point
    }

    fun persistLines(lines: List<Line>) {
        clearLines()
        for (line in lines) {
            val values = ContentValues()
            values.put(DbConstants.Lines.Columns.start_x.name, line.start?.x)
            values.put(DbConstants.Lines.Columns.start_y.name, line.start?.y)
            values.put(DbConstants.Lines.Columns.end_x.name, line.end?.x)
            values.put(DbConstants.Lines.Columns.end_y.name, line.end?.y)
            database!!.insert(DbConstants.Lines.DATABASE_TABLE, null, values)
        }
    }

    fun restoreLines(): MutableList<Line> {
        val lines: MutableList<Line> = ArrayList()
        val cursor: Cursor =
            database!!.query(DbConstants.Lines.DATABASE_TABLE, lineColumns, null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
            val line: Line = cursorToLine(cursor)
            lines.add(line)
            cursor.moveToNext()
        }
        cursor.close()
        return lines
    }

    fun clearLines() {
        database!!.delete(DbConstants.Lines.DATABASE_TABLE, null, null)
    }

    private fun cursorToLine(cursor: Cursor): Line {
        val line = Line()
        val startPoint = Point()
        startPoint.x =cursor.getFloat(DbConstants.Lines.Columns.start_x.ordinal)
        startPoint.y =cursor.getFloat(DbConstants.Lines.Columns.start_y.ordinal)
        line.start = startPoint
        val endPoint = Point()
        endPoint.x =cursor.getFloat(DbConstants.Lines.Columns.end_x.ordinal)
        endPoint.y =cursor.getFloat(DbConstants.Lines.Columns.end_y.ordinal)
        line.end = endPoint
        return line
    }

}