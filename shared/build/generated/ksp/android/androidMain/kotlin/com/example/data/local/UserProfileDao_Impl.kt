package com.example.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.model.UserProfile
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserProfileDao_Impl(
  __db: RoomDatabase,
) : UserProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserProfile: EntityInsertAdapter<UserProfile>

  private val __updateAdapterOfUserProfile: EntityDeleteOrUpdateAdapter<UserProfile>
  init {
    this.__db = __db
    this.__insertAdapterOfUserProfile = object : EntityInsertAdapter<UserProfile>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_profile` (`id`,`name`,`location`,`gotra`,`nakshatra`,`rashi`,`avatarUrl`,`pujasCount`,`animalsCount`,`totalContributedRupees`) VALUES (?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfile) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.location)
        statement.bindText(4, entity.gotra)
        statement.bindText(5, entity.nakshatra)
        statement.bindText(6, entity.rashi)
        statement.bindText(7, entity.avatarUrl)
        statement.bindLong(8, entity.pujasCount.toLong())
        statement.bindLong(9, entity.animalsCount.toLong())
        statement.bindLong(10, entity.totalContributedRupees.toLong())
      }
    }
    this.__updateAdapterOfUserProfile = object : EntityDeleteOrUpdateAdapter<UserProfile>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `user_profile` SET `id` = ?,`name` = ?,`location` = ?,`gotra` = ?,`nakshatra` = ?,`rashi` = ?,`avatarUrl` = ?,`pujasCount` = ?,`animalsCount` = ?,`totalContributedRupees` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UserProfile) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.location)
        statement.bindText(4, entity.gotra)
        statement.bindText(5, entity.nakshatra)
        statement.bindText(6, entity.rashi)
        statement.bindText(7, entity.avatarUrl)
        statement.bindLong(8, entity.pujasCount.toLong())
        statement.bindLong(9, entity.animalsCount.toLong())
        statement.bindLong(10, entity.totalContributedRupees.toLong())
        statement.bindText(11, entity.id)
      }
    }
  }

  public override suspend fun insertProfile(profile: UserProfile): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserProfile.insert(_connection, profile)
  }

  public override suspend fun updateProfile(profile: UserProfile): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfUserProfile.handle(_connection, profile)
  }

  public override fun getUserProfile(): Flow<UserProfile?> {
    val _sql: String = "SELECT * FROM user_profile LIMIT 1"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfGotra: Int = getColumnIndexOrThrow(_stmt, "gotra")
        val _columnIndexOfNakshatra: Int = getColumnIndexOrThrow(_stmt, "nakshatra")
        val _columnIndexOfRashi: Int = getColumnIndexOrThrow(_stmt, "rashi")
        val _columnIndexOfAvatarUrl: Int = getColumnIndexOrThrow(_stmt, "avatarUrl")
        val _columnIndexOfPujasCount: Int = getColumnIndexOrThrow(_stmt, "pujasCount")
        val _columnIndexOfAnimalsCount: Int = getColumnIndexOrThrow(_stmt, "animalsCount")
        val _columnIndexOfTotalContributedRupees: Int = getColumnIndexOrThrow(_stmt,
            "totalContributedRupees")
        val _result: UserProfile?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpGotra: String
          _tmpGotra = _stmt.getText(_columnIndexOfGotra)
          val _tmpNakshatra: String
          _tmpNakshatra = _stmt.getText(_columnIndexOfNakshatra)
          val _tmpRashi: String
          _tmpRashi = _stmt.getText(_columnIndexOfRashi)
          val _tmpAvatarUrl: String
          _tmpAvatarUrl = _stmt.getText(_columnIndexOfAvatarUrl)
          val _tmpPujasCount: Int
          _tmpPujasCount = _stmt.getLong(_columnIndexOfPujasCount).toInt()
          val _tmpAnimalsCount: Int
          _tmpAnimalsCount = _stmt.getLong(_columnIndexOfAnimalsCount).toInt()
          val _tmpTotalContributedRupees: Int
          _tmpTotalContributedRupees = _stmt.getLong(_columnIndexOfTotalContributedRupees).toInt()
          _result =
              UserProfile(_tmpId,_tmpName,_tmpLocation,_tmpGotra,_tmpNakshatra,_tmpRashi,_tmpAvatarUrl,_tmpPujasCount,_tmpAnimalsCount,_tmpTotalContributedRupees)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getUserProfileById(userId: String): Flow<UserProfile?> {
    val _sql: String = "SELECT * FROM user_profile WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("user_profile")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, userId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfGotra: Int = getColumnIndexOrThrow(_stmt, "gotra")
        val _columnIndexOfNakshatra: Int = getColumnIndexOrThrow(_stmt, "nakshatra")
        val _columnIndexOfRashi: Int = getColumnIndexOrThrow(_stmt, "rashi")
        val _columnIndexOfAvatarUrl: Int = getColumnIndexOrThrow(_stmt, "avatarUrl")
        val _columnIndexOfPujasCount: Int = getColumnIndexOrThrow(_stmt, "pujasCount")
        val _columnIndexOfAnimalsCount: Int = getColumnIndexOrThrow(_stmt, "animalsCount")
        val _columnIndexOfTotalContributedRupees: Int = getColumnIndexOrThrow(_stmt,
            "totalContributedRupees")
        val _result: UserProfile?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpGotra: String
          _tmpGotra = _stmt.getText(_columnIndexOfGotra)
          val _tmpNakshatra: String
          _tmpNakshatra = _stmt.getText(_columnIndexOfNakshatra)
          val _tmpRashi: String
          _tmpRashi = _stmt.getText(_columnIndexOfRashi)
          val _tmpAvatarUrl: String
          _tmpAvatarUrl = _stmt.getText(_columnIndexOfAvatarUrl)
          val _tmpPujasCount: Int
          _tmpPujasCount = _stmt.getLong(_columnIndexOfPujasCount).toInt()
          val _tmpAnimalsCount: Int
          _tmpAnimalsCount = _stmt.getLong(_columnIndexOfAnimalsCount).toInt()
          val _tmpTotalContributedRupees: Int
          _tmpTotalContributedRupees = _stmt.getLong(_columnIndexOfTotalContributedRupees).toInt()
          _result =
              UserProfile(_tmpId,_tmpName,_tmpLocation,_tmpGotra,_tmpNakshatra,_tmpRashi,_tmpAvatarUrl,_tmpPujasCount,_tmpAnimalsCount,_tmpTotalContributedRupees)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateSpiritualIdentity(
    gotra: String,
    nakshatra: String,
    rashi: String,
  ) {
    val _sql: String = "UPDATE user_profile SET gotra = ?, nakshatra = ?, rashi = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, gotra)
        _argIndex = 2
        _stmt.bindText(_argIndex, nakshatra)
        _argIndex = 3
        _stmt.bindText(_argIndex, rashi)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun incrementPujaCount() {
    val _sql: String = "UPDATE user_profile SET pujasCount = pujasCount + 1"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun addContribution(amount: Int) {
    val _sql: String = "UPDATE user_profile SET totalContributedRupees = totalContributedRupees + ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, amount.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
