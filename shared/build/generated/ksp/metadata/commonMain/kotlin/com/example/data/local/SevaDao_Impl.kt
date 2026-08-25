package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.model.SevaContribution
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SevaDao_Impl(
  __db: RoomDatabase,
) : SevaDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSevaContribution: EntityInsertAdapter<SevaContribution>
  init {
    this.__db = __db
    this.__insertAdapterOfSevaContribution = object : EntityInsertAdapter<SevaContribution>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `seva_contributions` (`id`,`title`,`targetType`,`targetName`,`amountRupees`,`sevaCategory`,`dateStr`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SevaContribution) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.targetType)
        statement.bindText(4, entity.targetName)
        statement.bindLong(5, entity.amountRupees.toLong())
        statement.bindText(6, entity.sevaCategory)
        statement.bindText(7, entity.dateStr)
        statement.bindLong(8, entity.timestamp)
      }
    }
  }

  public override suspend fun insertContribution(contribution: SevaContribution): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSevaContribution.insertAndReturnId(_connection,
        contribution)
    _result
  }

  public override fun getAllContributions(): Flow<List<SevaContribution>> {
    val _sql: String = "SELECT * FROM seva_contributions ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("seva_contributions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfTargetName: Int = getColumnIndexOrThrow(_stmt, "targetName")
        val _columnIndexOfAmountRupees: Int = getColumnIndexOrThrow(_stmt, "amountRupees")
        val _columnIndexOfSevaCategory: Int = getColumnIndexOrThrow(_stmt, "sevaCategory")
        val _columnIndexOfDateStr: Int = getColumnIndexOrThrow(_stmt, "dateStr")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<SevaContribution> = mutableListOf()
        while (_stmt.step()) {
          val _item: SevaContribution
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpTargetName: String
          _tmpTargetName = _stmt.getText(_columnIndexOfTargetName)
          val _tmpAmountRupees: Int
          _tmpAmountRupees = _stmt.getLong(_columnIndexOfAmountRupees).toInt()
          val _tmpSevaCategory: String
          _tmpSevaCategory = _stmt.getText(_columnIndexOfSevaCategory)
          val _tmpDateStr: String
          _tmpDateStr = _stmt.getText(_columnIndexOfDateStr)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              SevaContribution(_tmpId,_tmpTitle,_tmpTargetType,_tmpTargetName,_tmpAmountRupees,_tmpSevaCategory,_tmpDateStr,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSevaCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM seva_contributions"
    return createFlow(__db, false, arrayOf("seva_contributions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTotalAmountContributed(): Flow<Int> {
    val _sql: String = "SELECT COALESCE(SUM(amountRupees), 0) FROM seva_contributions"
    return createFlow(__db, false, arrayOf("seva_contributions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
