package com.project.kioasktab.connection

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

object DbManager {

     val url = "jdbc:mysql://awseb-e-dzjyjkpwnw-stack-awsebrdsdatabase-yeoaesngjksy.cm483yazbwfn.ap-northeast-2.rds.amazonaws.com/menu?serverTimezone=Asia/Seoul" // RDS 엔드포인트 또는 로컬 MySQL 서버 주소로 변경
     val user = "admin" // RDS 또는 로컬 MySQL 사용자명으로 변경
     val password = "admin12345" // RDS 또는 로컬 MySQL 암호로 변경

     fun executeQuery(query: String): ResultSet? {
        var connection: Connection? = null
        var statement: Statement? = null
        var resultSet: ResultSet? = null

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver")

            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, user, password)

            // 쿼리 실행
            statement = connection.createStatement()
            resultSet = statement.executeQuery(query)

            Log.w("zio","resultset: $resultSet")

            return resultSet
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("zio","error : ${e.message}")
        } finally {
            // 리소스 정리
            resultSet?.close()
            statement?.close()
            connection?.close()
        }

        return null
    }
}
