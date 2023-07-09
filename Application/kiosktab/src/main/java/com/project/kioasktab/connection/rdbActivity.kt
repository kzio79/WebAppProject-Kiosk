package com.project.kioasktab.connection

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.project.kioasktab.R
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class rdbActivity : AppCompatActivity() {
    private lateinit var connection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rdb)

        // 데이터베이스 접속
        connectToDatabase()
    }

    private fun connectToDatabase() {
        val DB_DRIVER = "com.mysql.jdbc.Driver"
        val DB_URL = "jdbc:mysql://awseb-e-pjwg4ikcam-stack-awsebrdsdatabase-gdns6ffwvbhl.cm483yazbwfn.ap-northeast-2.rds.amazonaws.com"
        val DB_USER = "admin"
        val DB_PASSWORD = "admin12345"

        try {
            // JDBC 드라이버 로드
            Class.forName(DB_DRIVER)

            // 데이터베이스 접속
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)

            // 데이터베이스 사용
            println("Database connection success.")
            Log.w("zio","성공")

        } catch (e: SQLException) {
            println("Database connection failure: ${e.message}")
            Log.w("zio","sql에러 : ${e.message}")

        } catch (e: ClassNotFoundException) {
            println("Load JDBC driver failure: ${e.message}")
            Log.w("zio","드라이버 로드 실패: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 데이터베이스 접속 종료
        connection.close()
    }

}